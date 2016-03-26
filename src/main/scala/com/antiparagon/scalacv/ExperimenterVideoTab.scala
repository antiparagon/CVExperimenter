package com.antiparagon.scalacv

import java.util.concurrent.{Executors, ScheduledExecutorService, TimeUnit}

import org.opencv.core.Mat
import org.opencv.videoio.{VideoCapture, Videoio}

import scalafx.Includes._
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, ScrollPane, Tab}
import scalafx.scene.image.{Image, ImageView, WritableImage}
import scalafx.scene.layout.{HBox, VBox}

/**
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
                      capture.open(0)
                      if (capture.isOpened()) {
                        cameraActive = true
                        capture.set(Videoio.CAP_PROP_FRAME_WIDTH, VIDEO_WIDTH)
                        capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, VIDEO_HEIGHT)
                        // grab a frame every 33 ms (30 frames/sec)
                        val frameGrabber = new Runnable() {
                          def run {
                            val image = grabFrame()
                            currentFrame.setImage(image)
                          }
                        }
                        timer = Executors.newSingleThreadScheduledExecutor()
                        timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS)
                        text = "Stop Camera"
                      } else {
                        println("Unable to open the camera connection...")
                      }
                    }
                    else {
                      cameraActive = false
                      text = "Start Camera"
                      try {
                        timer.shutdown()
                        timer.awaitTermination(33, TimeUnit.MILLISECONDS)
                      } catch {
                        case e: InterruptedException => println("Exception in stopping the frame capture, trying to release the camera now... " + e);
                      }
                      capture.release()
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

  def grabFrame(): Image = {
    var image: Image = null
    val frame = new Mat()
    if (capture.isOpened) {
      try {
        capture.read(frame)
        if (!frame.empty()) {
          // convert the image to gray scale
          //Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY)
          // convert the Mat object (OpenCV) to Image (JavaFX)
          image = ImageTools.converCVtoFX(frame)
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
