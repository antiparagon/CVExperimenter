package com.antiparagon.cvexperimenter.gui

import java.util.concurrent.{Executors, ScheduledExecutorService, TimeUnit}

import com.antiparagon.cvexperimenter.chessscanner.ChessScanner
import com.antiparagon.cvexperimenter.tools.ImageTools
import org.opencv.core.Mat
import org.opencv.videoio.{VideoCapture, Videoio}

import scalafx.Includes._
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, ScrollPane, Tab}
import scalafx.scene.image.{Image, ImageView, WritableImage}
import scalafx.scene.layout.{HBox, VBox}

/**
  * Tab that displays the video from webcam. The video is captured
  * as images in a thread and displayed in an ImageView.
  *
  * Created by wmckay on 3/13/16.
  */
class ExperimenterVideoTab() extends Tab with ExperimenterTab {

  val capture = new VideoCapture()
  var cameraActive = false
  val VIDEO_WIDTH = 720
  val VIDEO_HEIGHT = 405

  val currentFrame =  new ImageView(new WritableImage(VIDEO_WIDTH, VIDEO_HEIGHT))
  //currentFrame.fitWidth = 640
  //currentFrame.preserveRatio = true
  var timer: ScheduledExecutorService = null

  content = new VBox {
    padding = Insets(20)
    style = BACKGROUND_STYLE
    alignment = Pos.Center
    children = Seq(
      new HBox {
        alignment = Pos.Center
        children = new ScrollPane {
          style = BACKGROUND_STYLE
          content = new VBox {
            padding = Insets(20)
            style = BACKGROUND_STYLE
            spacing = 5
            children = Seq(
              currentFrame,
              new HBox {
                alignment = Pos.Center
                children = new Button {
                  text = "Start Video"
                  style = BUTTON_STYLE
                  onAction = handle {
                    if (!cameraActive) {
                      if(startVideo) {
                        text = "Stop Video"
                      }
                    } else {
                      text = "Start Video"
                      stopVideo
                    }
                  }
                }
              }
            )
          }
        }
      }
    )
  }

  def startVideo(): Boolean = {
    capture.open(0)
    if (capture.isOpened) {
      cameraActive = true
      capture.set(Videoio.CAP_PROP_FRAME_WIDTH, VIDEO_WIDTH)
      capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, VIDEO_HEIGHT)
      // grab a frame every 33 ms (30 frames/sec)
      val frameGrabber = new Runnable {
        def run {
          // Apply algorithm to image
          val image = ChessScanner.getChessboard(grabMatFrame)
          currentFrame.setImage(ImageTools.convertCVtoFX(image))
        }
      }
      timer = Executors.newSingleThreadScheduledExecutor()
      timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS)
      return true

    } else {
      println("Unable to open the camera connection...")
      return false
    }
  }

  /**
    * This is to insure the video is stopped.
    */
  def stopVideo(): Unit = {
    if(cameraActive) {
      try {
        timer.shutdown
        timer.awaitTermination(33, TimeUnit.MILLISECONDS)
      } catch {
        case e: InterruptedException => println("Exception in stopping the frame capture, trying to release the camera now... " + e);
      }
      cameraActive = false
      capture.release
    }
  }

  def grabMatFrame(): Mat = {
    val frame = new Mat
    if (capture.isOpened) {
      try {
        capture.read(frame)
      } catch {
        case e: Exception => println("Exception during the image elaboration: " + e)
      }
    }
    return frame
  }

  def grabImageFrame(): Image = {
    var image: Image = null
    val frame = new Mat()
    if (capture.isOpened) {
      try {
        capture.read(frame)
        if (!frame.empty) {
          image = ImageTools.convertCVtoFX(frame)
        }
      } catch {
        case e: Exception => println("Exception during the image elaboration: " + e)
      }
    }
    return image
  }

  override def getImg() : Image = {
    return currentFrame.image.value
  }

  override def getTabText(): String = text.value

}
