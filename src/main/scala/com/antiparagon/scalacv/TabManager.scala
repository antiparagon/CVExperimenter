package com.antiparagon.scalacv

import org.opencv.core.Mat

import scala.collection.mutable.ArrayBuffer
import scalafx.scene.control.TabPane
import scalafx.scene.image.Image
import scalafx.scene.layout.Priority

/**
  * Created by wmckay on 3/16/16.
  */
class TabManager {

  val tabs = ArrayBuffer.empty[ExperimenterImageTab]

  val imagePane = new TabPane {
    hgrow = Priority.Always
  }

  def isImageTabSelected(): Boolean = {
    val index = imagePane.getSelectionModel.getSelectedIndex
    if(index >= 0 && index < tabs.length) {
      return true
    }
    return false
  }

  def getSelectedIndex() : Int = {
    return imagePane.getSelectionModel.getSelectedIndex
  }

  def getSelectedImg(): Image = {
    if (isImageTabSelected()) {
      return tabs(getSelectedIndex()).getImg()
    }
    return null
  }

  def getSelectedMat(): Mat = {
    if (isImageTabSelected()) {
      return tabs(getSelectedIndex()).getMat()
    }
    return null
  }

  def getSelectedText(): String = {
    if (getSelectedIndex() >= 0) {
      return tabs(getSelectedIndex()).text.value
    }
    return ""
  }

  def addImageTab(name : String, img : Image): Unit = {
    val tab = new ExperimenterImageTab(img)
    tab.text = name
    imagePane += tab
    imagePane.selectionModel.value.select(tab)
    val index = imagePane.getSelectionModel.getSelectedIndex
    if(tabs.length - 1 < index) {
      tabs += tab
    } else {
      tabs(index) = tab
    }
  }

  def getTabPane() : TabPane = {
    return imagePane
  }
}
