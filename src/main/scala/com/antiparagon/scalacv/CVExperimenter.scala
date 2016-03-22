package com.antiparagon.scalacv

import java.io.IOException
import javax.imageio.ImageIO

import org.opencv.core._
import org.opencv.imgproc.Imgproc

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.embed.swing.SwingFXUtils
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{ScrollPane, Button}
import scalafx.scene.image._
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.Color._
import scalafx.stage.FileChooser

/**
  * Created by wmckay on 11/29/15.
  */
object CVExperimenter extends JFXApp {

  System.loadLibrary("opencv_java300")

  val tabManager: TabManager = new TabManager

  val controlPane = new ScrollPane {
    fitToHeight = true
    fitToWidth = true
    minViewportWidth = 250.0
    style = "-fx-background-color: black"
    content = new VBox {
      padding = Insets(20)
      style = "-fx-background-color: black"
      children = Seq(
        new Button {
          text = "Open Image..."
          style = "-fx-font-size: 20pt"
          onAction = handle {
            val fileChooser = new FileChooser() {
              title = "Pick an Image File"
            }
            val img = fileChooser.showOpenDialog(scene.window())
            if (img != null) {
              tabManager.addImageTab(img.getName, new Image(img.toURI.toString))
            }
          }
        },
        new Button {
          text = "Save Image..."
          style = "-fx-font-size: 20pt"
          onAction = handle {
            if(tabManager.isImageTabSelected()) {
              val fileChooser = new FileChooser() {
                title = "Save Image File"
              }
              val img = fileChooser.showSaveDialog(scene.window())
              if (img != null) {
                println("Image name: " + img.getName())
                var imgType = "png"
                if (img.getName.endsWith(".jpg")) imgType = "jpg"
                if (img.getName.endsWith(".gif")) imgType = "gif"
                if (img.getName.endsWith(".bmp")) imgType = "bmp"
                if (img.getName.endsWith(".png")) imgType = "png"
                try {
                  ImageIO.write(SwingFXUtils.fromFXImage(tabManager.getSelectedImg(), null), imgType, img)
                } catch {
                  case ex: IOException => {
                    ex.printStackTrace()
                    println("Unable to save inmage to:" + img.getName() + " - " + ex.getMessage())
                  }
                }
              }
            }
          }
        },
        new Button {
          text = "Open Webcam..."
          style = "-fx-font-size: 20pt"
          onAction = handle {
            tabManager.addVideoTab("Video Tab")
          }
        },
        new Button {
          text = "Apply Outline"
          style = "-fx-font-size: 20pt"
          onAction = handle {
            if(tabManager.isImageTabSelected()) {
              val image: Mat = tabManager.getSelectedMat()
              val imageHSV = new Mat(image.size(), 1)
              val imageBlurr = new Mat(image.size(), 1)
              val imageA = new Mat(image.size(), 127)
              //val imageB = new Mat(image.size(), 1)
              //imageB.setTo(new Scalar(255,255,255))
              Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY)
              Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5, 5), 0)
              Imgproc.adaptiveThreshold(imageBlurr, imageA, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 7, 5);

              tabManager.addImageTab(tabManager.getSelectedText() + " - mod", ImageTools.converCVtoFX(imageA))
            }
          }
        },
        new Button {
          text = "translate 10 10"
          style = "-fx-font-size: 20pt"
          onAction = handle {
            if(tabManager.isImageTabSelected()) {
              val image = tabManager.getSelectedMat()
              val trans = ImageTools.translate(image, 10.0, 10.0)

              tabManager.addImageTab(tabManager.getSelectedText() + " - trans", ImageTools.converCVtoFX(trans))
            }
          }
        }

      )
    }
  }

  def getLayout() : HBox = {
    val hbox = new HBox {
      padding = Insets(20)
      style = "-fx-background-color: black"
      children = Seq(
        controlPane,
        tabManager.getTabPane()
      )
    }

    return hbox
  }

  stage = new PrimaryStage {
    title = "CV Experimenter"
    icons += new Image(CVExperimenter.getClass.getClassLoader.getResourceAsStream("icon.png"))
    scene = new Scene(1500,750) {
      fill = Black
      root = getLayout()
    }
  }


}
