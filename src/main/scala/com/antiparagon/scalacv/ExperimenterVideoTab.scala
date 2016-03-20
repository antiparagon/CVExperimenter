package com.antiparagon.scalacv

import java.util.concurrent.{ScheduledExecutorService, TimeUnit, Executors}

import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import org.opencv.videoio.VideoCapture

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
              // start the video capture
              capture.open(0);
              // is the video stream available?
              if (capture.isOpened()) {
                cameraActive = true

                // grab a frame every 33 ms (30 frames/sec)
                val frameGrabber = new Runnable() {
                  def run {
                    val imageToShow = grabFrame()
                    currentFrame.setImage(imageToShow)
                  }
                }

                timer = Executors.newSingleThreadScheduledExecutor()
                timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS)

                // update the button content
                text = "Stop Camera"
              }
              else {
                // log the error
                println("Impossible to open the camera connection...")
              }
            }
            else {
              // the camera is not active at this point
              cameraActive = false
              // update again the button content
              text = "Start Camera"

              // stop the timer
              try  {
                timer.shutdown();
                timer.awaitTermination(33, TimeUnit.MILLISECONDS);
              }
              catch {
                // log the exception
                case e: InterruptedException => println("Exception in stopping the frame capture, trying to release the camera now... " + e);
              }

              // release the camera
              capture.release();
              // clean the frame
              //currentFrame.setImage(null);
            }

          }
        }
      )
    }
  }

  def grabFrame(): Image = {
    // init everything
    var imageToShow: Image = null
    val frame = new Mat()

    // check if the capture is open
    if (capture.isOpened) {
      try {
        // read the current frame
        capture.read(frame)

        // if the frame is not empty, process it
        if (!frame.empty()) {
          // convert the image to gray scale
          //Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY)
          // convert the Mat object (OpenCV) to Image (JavaFX)
          imageToShow = ImageTools.converCVtoFX(frame)
        }

      } catch {
        // log the error
        case e: Exception => println("Exception during the image elaboration: " + e)
      }
    }

    return imageToShow
  }

  override def getImg() : Image = {
    return currentFrame.image.value
  }

  override def getTabText(): String = text.value
}
