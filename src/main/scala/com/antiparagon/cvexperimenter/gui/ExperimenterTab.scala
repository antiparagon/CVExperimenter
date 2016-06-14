package com.antiparagon.cvexperimenter.gui

import com.antiparagon.cvexperimenter.tools.ImageTools
import org.opencv.core.Mat

import scalafx.scene.image.Image

/**
  * Trait for tabs.
  *
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
