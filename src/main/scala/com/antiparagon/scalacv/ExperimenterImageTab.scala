package com.antiparagon.scalacv

import org.opencv.core.Mat

import scalafx.geometry.Insets
import scalafx.scene.control.{ScrollPane, Tab}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.VBox

/**
  * Created by wmckay on 3/13/16.
  */
class ExperimenterImageTab(val img : Image) extends Tab with ExperimenterTab {

  content = new VBox {
    padding = Insets(20)
    style = "-fx-background-color: black"
    children = Seq(
      new ScrollPane {
        style = "-fx-background-color: black"
        val imgView = new ImageView(img)
        imgView.style = "-fx-background-color: transparent"
        content = imgView
      }
    )
  }

  override def getImg() : Image = return img

  override def getTabText(): String = text.value
}
