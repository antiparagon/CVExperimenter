package com.antiparagon.scalacv

import org.opencv.core.Mat

import scalafx.scene.image.Image

/**
  * Created by wmckay on 3/19/16.
  */
trait ExperimenterTab {

  def getTabText() : String

  def getImg() : Image

  def getMat() : Mat = {
    ImageTools.converFXtoCV(getImg())
  }
}
