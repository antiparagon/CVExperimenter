package com.antiparagon.scalacv

import org.opencv.core.Mat

import scala.collection.mutable.ArrayBuffer

import scalafx.Includes._
import scalafx.scene.control.TabPane
import scalafx.scene.image.Image
import scalafx.scene.layout.Priority

/**
  * Created by wmckay on 3/16/16.
  */
class TabManager {

  val tabs = ArrayBuffer.empty[ExperimenterTab]

  val tabPane = new TabPane {
    hgrow = Priority.Always
  }

  def getSelectedTabWidth() : Double = {
    tabPane.maxWidth()
  }

  def getSelectedTabHeight() : Double = {
    tabPane.maxHeight()
  }

  def isImageTabSelected(): Boolean = {
    val index = tabPane.getSelectionModel.getSelectedIndex
    if(index >= 0 && index < tabs.length) {
      return true
    }
    return false
  }

  def getSelectedIndex() : Int = {
    return tabPane.getSelectionModel.getSelectedIndex
  }

  def getSelectedImg(): Image = {
    if (isImageTabSelected) {
      return tabs(getSelectedIndex).getImg
    }
    return null
  }

  def getSelectedMat(): Mat = {
    if (isImageTabSelected) {
      return tabs(getSelectedIndex).getMat
    }
    return null
  }

  def getSelectedText(): String = {
    if (getSelectedIndex() >= 0) {
      return tabs(getSelectedIndex).getTabText
    }
    return ""
  }

  def hasVideoTab(): Boolean = {
    tabs.foreach(tab => {
      if(tab.isInstanceOf[ExperimenterVideoTab]) {
        return true
      }
    })
    return false
  }

  def showVideoTab(): Unit = {
    tabs.foreach(tab => {
      if(tab.isInstanceOf[ExperimenterVideoTab]) {
        tabPane.getSelectionModel.select(tab.asInstanceOf[ExperimenterVideoTab])
      }
    })
  }

  def stopVideoTabs(): Unit = {
    tabs.foreach(tab => {
      if(tab.isInstanceOf[ExperimenterVideoTab]) {
        tab.asInstanceOf[ExperimenterVideoTab].stopVideo()
      }
    })
  }

  def addChessScannerTab(name : String, img : Image): Unit = {
    val tab = new ChessScannerTab(img)

    tab.onClosed = handle { tabs.remove(tabs.indexOf(tab)) }

    tab.text = name
    tabPane += tab
    tabPane.selectionModel.value.select(tab)
    val index = tabPane.getSelectionModel.getSelectedIndex
    if(tabs.length - 1 < index) {
      tabs += tab
    } else {
      tabs(index) = tab
    }
  }

  def addImageTab(name : String, img : Image): Unit = {
    val tab = new ExperimenterImageTab(img)

    tab.onClosed = handle { tabs.remove(tabs.indexOf(tab)) }

    tab.text = name
    tabPane += tab
    tabPane.selectionModel.value.select(tab)
    val index = tabPane.getSelectionModel.getSelectedIndex
    if(tabs.length - 1 < index) {
      tabs += tab
    } else {
      tabs(index) = tab
    }
  }

  def addVideoTab(name : String): Unit = {
    val tab = new ExperimenterVideoTab()

    tab.onClosed = handle {
      tab.stopVideo()
      tabs.remove(tabs.indexOf(tab))
    }

    tab.text = name
    tabPane += tab
    tabPane.selectionModel.value.select(tab)
    val index = tabPane.getSelectionModel.getSelectedIndex
    if(tabs.length - 1 < index) {
      tabs += tab
    } else {
      tabs(index) = tab
    }
  }

  def getTabPane() : TabPane = {
    return tabPane
  }
}
