package com.antiparagon.cvexperimenter

import java.io.IOException
import javax.imageio.ImageIO

import org.opencv.core._
import org.opencv.imgproc.Imgproc

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.embed.swing.SwingFXUtils
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ScrollPane}
import scalafx.scene.image._
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.Color._
import scalafx.stage.FileChooser

/**
  * Created by wmckay on 11/29/15.
  */
object CVExperimenter extends JFXApp {

  System.loadLibrary("opencv_java300")

  val tabManager: TabManager = new TabManager
  val BUTTON_STYLE = "-fx-font-size: 14pt"
  val BACKGROUND_STYLE = "-fx-background-color: black"

  val controlPane = new ScrollPane {
    fitToHeight = true
    fitToWidth = true
    minViewportWidth = 200.0
    style = BACKGROUND_STYLE
    content = new VBox {
      padding = Insets(20)
      spacing = 5
      style = BACKGROUND_STYLE
      children = (new CommandPanel(tabManager)).getChildren()
    }
  }

  def getLayout() : HBox = {
    val hbox = new HBox {
      padding = Insets(0, 0, 10, 10)
      style = BACKGROUND_STYLE
      children = Seq(
        controlPane,
        tabManager.getTabPane
      )
    }

    return hbox
  }


  stage = new PrimaryStage {
    title = "CV Experimenter"
    icons += new Image(CVExperimenter.getClass.getClassLoader.getResourceAsStream("icon.png"))
    scene = new Scene(1400,850) {
      fill = Black
      root = getLayout
    }
    tabManager.addImageTab("Logo", new Image(CVExperimenter.getClass.getClassLoader.getResourceAsStream("CVExperimenterLogo.png")))
  }

  override def stopApp(): Unit = {
    println("Stopping")
    tabManager.stopVideoTab
    super.stopApp
  }
}
