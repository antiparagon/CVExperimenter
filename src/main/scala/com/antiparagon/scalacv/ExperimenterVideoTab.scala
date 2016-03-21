package com.antiparagon.scalacv

import java.util.concurrent.{ScheduledExecutorService, TimeUnit, Executors}

import org.opencv.core.Mat
import org.opencv.videoio.{VideoCapture}

import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, ScrollPane, Tab}
import scalafx.scene.image.{WritableImage, Image, ImageView}
import scalafx.scene.layout.VBox

/**
  * Created by wmckay on 3/13/16.
  */
class ExperimenterVideoTab() extends Tab with ExperimenterTab {

  val capture = new VideoCapture()
  var cameraActive = false
  val currentFrame = {
    new ImageView(new WritableImage(500, 500))
  }
  var timer: ScheduledExecutorService = null

  content = new ScrollPane {
    style = "-fx-background-color: black"
    content = new VBox {
      padding = Insets(20)
      style = "-fx-background-color: black"
      children = Seq(
        currentFrame,
        new Button {
          text = "Start Video"
          style = "-fx-font-size: 20pt"
          onAction = handle {
            if (!cameraActive) {
              capture.open(0)
              if (capture.isOpened()) {
                cameraActive = true
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
                println("Impossible to open the camera connection...")
              }
            }
            else {
              cameraActive = false
              text = "Start Camera"
              try  {
                timer.shutdown()
                timer.awaitTermination(33, TimeUnit.MILLISECONDS)
              } catch {
                case e: InterruptedException => println("Exception in stopping the frame capture, trying to release the camera now... " + e);
              }
              capture.release()
            }
          }
        }
      )
    }
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
