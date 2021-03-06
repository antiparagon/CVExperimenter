package com.antiparagon.cvexperimenter.gui

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{ScrollPane, Tab}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{HBox, VBox}

/**
  * Basic tab to display an image.
  *
  * Created by wmckay on 3/13/16.
  */
class ExperimenterImageTab(val img : Image) extends Tab with ExperimenterTab {

  content = new VBox {
    padding = Insets(20)
    style = "-fx-background-color: black"
    alignment = Pos.Center
    children = Seq(
      new HBox {
        alignment = Pos.Center
        children = new ScrollPane {
          style = "-fx-background-color: black"
          //alignment = Pos.Center
          val imgView = new ImageView(img)
          imgView.style = "-fx-background-color: transparent"
          content = imgView
        }
      }
    )
  }

  override def getImg() : Image = return img

  override def getTabText(): String = text.value

}
