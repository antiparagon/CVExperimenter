package com.antiparagon.scalacv

import java.io.IOException
import javax.imageio.ImageIO

import org.opencv.core.{Mat, Size}
import org.opencv.imgproc.Imgproc

import scalafx.Includes._
import scalafx.embed.swing.SwingFXUtils
import scalafx.scene.Node
import scalafx.scene.control.Button
import scalafx.scene.image.Image
import scalafx.stage.FileChooser

/**
  * Created by wmckay on 5/4/16.
  */
class CommandPanel(tabManager: TabManager) {

  val BUTTON_STYLE = "-fx-font-size: 14pt"

  def getChildren(): Seq[Node] = {
    return Seq(
      new Button {
        text = "Open Image..."
        style = BUTTON_STYLE
        maxWidth = Double.MaxValue
        onAction = handle {
          val fileChooser = new FileChooser {
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
          if(tabManager.isImageTabSelected) {
            val fileChooser = new FileChooser {
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
                ImageIO.write(SwingFXUtils.fromFXImage(tabManager.getSelectedImg, null), imgType, img)
              } catch {
                case ex: IOException => {
                  ex.printStackTrace
                  println("Unable to save image to:" + img.getName + " - " + ex.getMessage)
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
          if(tabManager.hasVideoTab) {
            tabManager.showVideoTab
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
          if(tabManager.isImageTabSelected) {
            val image: Mat = tabManager.getSelectedMat
            val imageHSV = new Mat(image.size, 1)
            val imageBlurr = new Mat(image.size, 1)
            val imageA = new Mat(image.size, 127)
            //val imageB = new Mat(image.size(), 1)
            //imageB.setTo(new Scalar(255,255,255))
            Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY)
            Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5, 5), 0)
            Imgproc.adaptiveThreshold(imageBlurr, imageA, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 7, 5)

            tabManager.addImageTab(tabManager.getSelectedText + " - mod", ImageTools.convertCVtoFX(imageA))
          }
        }
      },

      new Button {
        text = "Chess Scanner"
        style = BUTTON_STYLE
        maxWidth = Double.MaxValue
        onAction = handle {
          if(tabManager.isImageTabSelected) {
            val image = tabManager.getSelectedMat
            val result = ChessScanner.findBoard(image)
            tabManager.addImageTab(tabManager.getSelectedText + " - chess", ImageTools.convertCVtoFX(result))
          }
        }
      }

    )
  }
}
