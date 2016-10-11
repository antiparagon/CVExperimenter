package com.antiparagon.cvexperimenter.gui

import org.opencv.core.Mat

import scala.collection.mutable.ArrayBuffer
import scalafx.Includes._
import scalafx.scene.control._
import scalafx.scene.image.Image
import scalafx.scene.layout.Priority

/**
  * Controls all the visible tabs.
  *
  * Created by wmckay on 3/16/16.
  */
class TabManager {

  val tabs = ArrayBuffer.empty[ExperimenterTab]

  val tabPane = new TabPane {
    hgrow = Priority.Always
  }

  def getTabPaneWidth() : Double = {
    tabPane.getBoundsInParent.getWidth
  }

  def getTabPaneHeight() : Double = {
    tabPane.getBoundsInParent.getHeight
  }

  def isTabSelected(): Boolean = {
    val index = tabPane.getSelectionModel.getSelectedIndex
    if(index >= 0 && index < tabs.length) {
      return true
    }
    false
  }

  def getSelectedIndex() : Int = {
    return tabPane.getSelectionModel.getSelectedIndex
  }

  def getSelectedImg(): Image = {
    if (isTabSelected) {
      return tabs(getSelectedIndex).getImg
    }
    null
  }

  def getSelectedMat(): Mat = {
    if (isTabSelected) {
      return tabs(getSelectedIndex).getMat
    }
    null
  }

  def getSelectedText(): String = {
    if (getSelectedIndex >= 0) {
      return tabs(getSelectedIndex).getTabText
    }
    ""
  }

  def hasVideoTab(): Boolean = {
    tabs.foreach(tab => {
      if(tab.isInstanceOf[ExperimenterVideoTab]) {
        return true
      }
    })
    false
  }

  def showVideoTab(): Unit = {
    tabs.foreach(tab => {
      if(tab.isInstanceOf[ExperimenterVideoTab]) {
        tabPane.getSelectionModel.select(tab.asInstanceOf[ExperimenterVideoTab])
      }
    })
  }

  def stopVideoTab(): Unit = {
    tabs.foreach(tab => {
      if(tab.isInstanceOf[ExperimenterVideoTab]) {
        tab.asInstanceOf[ExperimenterVideoTab].stopVideo()
      }
    })
  }

  def addImageEditorTab(name : String, img : Image): Unit = {
    val tab = new ImageEditorTab(img)
    tab.text = name
    tab.onClosed = handle { tabs.remove(tabs.indexOf(tab)) }
    addTab(tab)
  }

  def addChessScannerTab(name : String, img : Image): Unit = {
    val tab = new ChessScannerTab(img)
    tab.text = name
    tab.onClosed = handle { tabs.remove(tabs.indexOf(tab)) }
    addTab(tab)
  }

  def addChessScannerTestTab(name : String, img : Image): Unit = {
    val tab = new ChessScannerTestTab(img)
    tab.text = name
    tab.onClosed = handle { tabs.remove(tabs.indexOf(tab)) }
    addTab(tab)
  }

  def addImageTab(name : String, img : Image): Unit = {
    val tab = new ExperimenterImageTab(img)
    tab.text = name
    tab.onClosed = handle { tabs.remove(tabs.indexOf(tab)) }
    addTab(tab)
  }

  def addDebugImageTab(name : String, img : Image): Unit = {
    val tab = new ExperimenterImageTab(img)
    tab.text = name
    tab.onClosed = handle { tabs.remove(tabs.indexOf(tab)) }
    addDebugTab(tab)
  }

  def addVideoTab(name : String): Unit = {
    val tab = new ExperimenterVideoTab()
    tab.text = name
    tab.onClosed = handle {
      tab.stopVideo()
      tabs.remove(tabs.indexOf(tab))
    }
    addTab(tab)
  }

  def addTab(tab : Tab with ExperimenterTab): Unit = {
    tabPane += tab
    tabPane.selectionModel.value.select(tab)
    val index = tabPane.getSelectionModel.getSelectedIndex
    if(tabs.length - 1 < index) {
      tabs += tab
    } else {
      tabs(index) = tab
    }
  }

  def addDebugTab(tab : Tab with ExperimenterTab): Unit = {
    tabPane += tab
    val index = tabPane.getTabs.size - 1
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
