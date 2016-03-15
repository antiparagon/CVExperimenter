package com.antiparagon.scalacv

import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO

import org.opencv.core._
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc

import scala.collection.mutable.ArrayBuffer
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.embed.swing.SwingFXUtils
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Tab, TabPane, ScrollPane, Button}
import scalafx.scene.effect.DropShadow
import scalafx.scene.image._
import scalafx.scene.layout.{Priority, HBox, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.paint.{Stops, LinearGradient}
import scalafx.scene.text.Text
import scalafx.stage.FileChooser

/**
  * Created by wmckay on 11/29/15.
  */
object CVExperimenter extends JFXApp {

  System.loadLibrary("opencv_java300")

  val tabs = ArrayBuffer.empty[ExperimenterTab]

  val imagePane = new TabPane {
    hgrow = Priority.Always
  }

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
              val tab = new ExperimenterTab(new Image(img.toURI.toString))
              tab.text = img.getName
              imagePane += tab
              imagePane.selectionModel.value.select(tab)
              val index = imagePane.getSelectionModel.getSelectedIndex
              if(tabs.length - 1 < index) {
                tabs += tab
              } else {
                tabs(index) = tab
              }
            }
          }
        },
        new Button {
          text = "Save Image..."
          style = "-fx-font-size: 20pt"
          onAction = handle {
            val index = imagePane.getSelectionModel.getSelectedIndex
            println(s"Index $index")
            if(index >= 0 && index < tabs.length) {
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
                  ImageIO.write(SwingFXUtils.fromFXImage(tabs(index).getImg(), null), imgType, img)
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
          text = "Apply Outline"
          style = "-fx-font-size: 20pt"
          onAction = handle {
            val index = imagePane.getSelectionModel.getSelectedIndex
            if(index >= 0) {
              val tab = tabs(index)
              val image: Mat = tab.getMat()
              val imageHSV = new Mat(image.size(), 1)
              val imageBlurr = new Mat(image.size(), 1)
              val imageA = new Mat(image.size(), 127)
              //val imageB = new Mat(image.size(), 1)
              //imageB.setTo(new Scalar(255,255,255))
              Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY)
              Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5, 5), 0)
              Imgproc.adaptiveThreshold(imageBlurr, imageA, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 7, 5);

              val etab = new ExperimenterTab(converCVtoFX(imageA))
              etab.text = tab.getText + " - mod"
              imagePane += etab
              imagePane.selectionModel.value.select(etab)
              val i = imagePane.getSelectionModel.getSelectedIndex
              if(tabs.length - 1 < i) {
                tabs += etab
              } else {
                tabs(i) = etab
              }
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
        imagePane
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

  def converCVtoFX(mat:Mat) : WritableImage = {
    var img_type = BufferedImage.TYPE_BYTE_GRAY
    if ( mat.channels() > 1 ) {
      img_type = BufferedImage.TYPE_3BYTE_BGR
    }

    val array_size = mat.channels() * mat.cols() * mat.rows()
    val b = new Array[Byte](array_size)
    mat.get(0, 0, b)
    val view_image: BufferedImage  = new BufferedImage(mat.cols(), mat.rows(), img_type)
    view_image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), b)
    SwingFXUtils.toFXImage(view_image, null)
  }

  def converFXtoCV(img:Image) : Mat = {
    val width = img.width.value.toInt
    val height = img.height.value.toInt
    val buffer = new Array[Byte](width * height * 4)

    val reader = img.getPixelReader
    val format = PixelFormat.getByteBgraInstance
    reader.getPixels(0, 0, width, height, format, buffer, 0, width * 4)

    val mat = new Mat(height, width, CvType.CV_8UC4)
    mat.put(0, 0, buffer)
    return mat
  }
}
