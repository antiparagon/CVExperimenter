package com.antiparagon.cvexperimenter.gui

import java.io.IOException
import javax.imageio.ImageIO

import com.antiparagon.cvexperimenter.tools.{ImageDft, ImageTools}

import scalafx.Includes._
import scalafx.embed.swing.SwingFXUtils
import scalafx.geometry.Pos
import scalafx.scene.Node
import scalafx.scene.control.Button
import scalafx.scene.image.Image
import scalafx.scene.layout.HBox
import scalafx.stage.FileChooser

/**
  * Holds all the GUI widgets for CVExperimenter.
  *
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
          if(tabManager.isTabSelected) {
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
        text = "Resize Image"
        style = BUTTON_STYLE
        maxWidth = Double.MaxValue
        onAction = handle {
          if(tabManager.isTabSelected) {
            val image = tabManager.getSelectedMat
            val tabWidth = tabManager.getTabPaneWidth - 50 // For TabPane non-tab area
            val tabHeight = tabManager.getTabPaneHeight - 75 // For TabPane non-tab area
            var widthRatio = 1.0
            var heightRatio = 1.0
            if(image.width > tabWidth) {
              widthRatio = tabWidth / image.width
            }
            if(image.height > tabHeight) {
              heightRatio = tabHeight / image.height
            }
            if(widthRatio < heightRatio) {
              val result = ImageTools.resize(image, tabWidth.toInt, (image.height * widthRatio).toInt)
              tabManager.addImageTab(tabManager.getSelectedText + " - resized", ImageTools.convertCVtoFX(result))
            }
            if(heightRatio < widthRatio) {
              val result = ImageTools.resize(image, (image.width * heightRatio).toInt, tabHeight.toInt)
              tabManager.addImageTab(tabManager.getSelectedText + " - resized", ImageTools.convertCVtoFX(result))
            }
          }
        }
      },

      new HBox {
        spacing = 5
        alignment = Pos.Center
        children = Seq(
          new Button {
            text = "DFT"
            style = BUTTON_STYLE
            maxWidth = Double.MaxValue
            onAction = handle {
              if (tabManager.isTabSelected) {
                val image = tabManager.getSelectedMat
                val result = ImageDft.transformImage(image)
                tabManager.addImageTab(tabManager.getSelectedText + " - dft", ImageTools.convertCVtoFX(result))
              }
            }
          },

          new Button {
            text = "IDFT"
            style = BUTTON_STYLE
            maxWidth = Double.MaxValue
            onAction = handle {
              if (tabManager.isTabSelected) {
                val image = tabManager.getSelectedMat
                val result = ImageDft.antitransformImage(image)
                tabManager.addImageTab(tabManager.getSelectedText + " - idft", ImageTools.convertCVtoFX(result))
              }
            }
          })
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
        text = "Image Editor"
        style = BUTTON_STYLE
        maxWidth = Double.MaxValue
        onAction = handle {
          if(tabManager.isTabSelected) {
            val image = tabManager.getSelectedImg
            tabManager.addImageEditorTab(tabManager.getSelectedText + " - edit", image)
          }
        }
      },

      new Button {
        text = "Chess Scanner"
        style = BUTTON_STYLE
        maxWidth = Double.MaxValue
        onAction = handle {
          if(tabManager.isTabSelected) {
            val image = tabManager.getSelectedImg
            tabManager.addChessScannerTab(tabManager.getSelectedText + " - chess", image)
          }
        }
      }

//      new Button {
//        text = "Chess Scanner Test"
//        style = BUTTON_STYLE
//        maxWidth = Double.MaxValue
//        onAction = handle {
//          if(tabManager.isTabSelected) {
//            val image = tabManager.getSelectedImg
//            tabManager.addChessScannerTestTab(tabManager.getSelectedText + " - test", image)
//          }
//        }
//      }

    )
  }
}
