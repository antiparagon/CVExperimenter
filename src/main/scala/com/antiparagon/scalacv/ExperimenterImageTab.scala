package com.antiparagon.scalacv

import org.opencv.core.Mat

import scalafx.scene.control.{ScrollPane, Tab}
import scalafx.scene.image.{Image, ImageView}

/**
  * Created by wmckay on 3/13/16.
  */
class ExperimenterImageTab(val img : Image) extends Tab {

  content = new ScrollPane {
    style = "-fx-background-color: black"
    content = new ImageView(img)
  }

  def getImg() : Image = return img

  def getMat() : Mat = {
    ImageTools.converFXtoCV(img)
  }
}
