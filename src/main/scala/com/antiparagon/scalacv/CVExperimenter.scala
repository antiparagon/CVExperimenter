package com.antiparagon.scalacv

import java.io.IOException
import javax.imageio.ImageIO

import org.opencv.core._
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.embed.swing.SwingFXUtils
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ScrollPane}
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
  val BUTTON_STYLE = "-fx-font-size: 14pt"
  val BACKGROUND_STYLE = "-fx-background-color: black"

  val controlPane = new ScrollPane {
    fitToHeight = true
    fitToWidth = true
    minViewportWidth = 200.0
    style = BACKGROUND_STYLE
    content = new VBox {
      padding = Insets(20)
      spacing = 5
      style = BACKGROUND_STYLE
      children = Seq(
        new Button {
          text = "Open Image..."
          style = BUTTON_STYLE
          maxWidth = Double.MaxValue
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
          style = BUTTON_STYLE
          maxWidth = Double.MaxValue
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
          style = BUTTON_STYLE
          maxWidth = Double.MaxValue
          onAction = handle {
            if(tabManager.hasVideoTab()) {
              tabManager.showVideoTab()
            } else {
              tabManager.addVideoTab("Video Tab")
            }
          }
        },
        new Button {
          text = "Apply Outline"
          style = BUTTON_STYLE
          maxWidth = Double.MaxValue
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

              tabManager.addImageTab(tabManager.getSelectedText() + " - mod", ImageTools.convertCVtoFX(imageA))
            }
          }
        },
        new Button {
          text = "Translate 10 10"
          style = BUTTON_STYLE
          maxWidth = Double.MaxValue
          onAction = handle {
            if(tabManager.isImageTabSelected()) {
              val image = tabManager.getSelectedMat()
              val trans = ImageTools.translate(image, 10.0, 10.0)

              tabManager.addImageTab(tabManager.getSelectedText() + " - trans", ImageTools.convertCVtoFX(trans))
            }
          }
        },
        new Button {
          text = "Rotate 45"
          style = BUTTON_STYLE
          maxWidth = Double.MaxValue
          onAction = handle {
            if(tabManager.isImageTabSelected()) {
              val image = tabManager.getSelectedMat()
              ImageTools.outputMatProperties(image)
              val rot = ImageTools.rotate(image, -45.0, new Point(image.width()/2, image.height()/2))
              ImageTools.outputMatProperties(rot)

              tabManager.addImageTab(tabManager.getSelectedText() + " - rot", ImageTools.convertCVtoFX(rot))
            }
          }
        },

        new Button {
          text = "Rotate Test..."
          style = BUTTON_STYLE
          maxWidth = Double.MaxValue
          onAction = handle {
            val fileChooser = new FileChooser() {
              title = "Pick an Image File"
            }
            val img = fileChooser.showOpenDialog(scene.window())
            if (img != null) {
              println(s"Opening: ${img.getAbsolutePath}")
              val mat = Imgcodecs.imread(img.getAbsolutePath)
              ImageTools.outputMatProperties(mat)
              //tabManager.addImageTab(img.getName, ImageTools.converCVtoFX(mat))

              println(s"Mat size: ${mat.size()}")
              val center = new Point(mat.cols()/2.0, mat.rows()/2.0)
              val rot = Imgproc.getRotationMatrix2D(center, 20, 1.0)
              ImageTools.outputMatProperties(rot)
              val rotated = new Mat

              Imgproc.warpAffine(mat, mat, rot, mat.size)

              tabManager.addImageTab(img.getName, ImageTools.convertCVtoFX(mat))
            }
          }
        }

      )
    }
  }

  def getLayout() : HBox = {
    val hbox = new HBox {
      padding = Insets(0, 0, 10, 10)
      style = BACKGROUND_STYLE
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
    scene = new Scene(1400,850) {
      fill = Black
      root = getLayout()
    }
    tabManager.addImageTab("Logo", new Image(CVExperimenter.getClass.getClassLoader.getResourceAsStream("CVExperimenterLogo.png")))
  }

  override def stopApp(): Unit = {
    println("Stopping")
    tabManager.stopVideoTabs()
    super.stopApp()
  }
}
