package com.antiparagon.cvexperimenter

import org.opencv.core.Mat

import scalafx.scene.image.Image

/**
  * Created by wmckay on 3/19/16.
  */
trait ExperimenterTab {

  val BUTTON_STYLE = "-fx-font-size: 18pt"
  val BACKGROUND_STYLE = "-fx-background-color: black"

  def getTabText() : String

  def getImg() : Image

  def getMat() : Mat = {
    ImageTools.convertFXtoCV(getImg)
  }
}
