package com.antiparagon.cvexperimenter

import com.antiparagon.cvexperimenter.gui.{CommandPanel, TabManager}
import com.typesafe.scalalogging.Logger
import org.opencv.core.Core
import org.slf4j.LoggerFactory

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.ScrollPane
import scalafx.scene.image._
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.Color._

/**
  * Main class for CVExperimenter.
  *
  * Created by wmckay on 11/29/15.
  */
object CVExperimenter extends JFXApp {

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

  val log = Logger(LoggerFactory.getLogger("CVExperimenter"))

  val tabManager: TabManager = new TabManager
  val BUTTON_STYLE = "-fx-font-size: 14pt"
  val BACKGROUND_STYLE = "-fx-background-color: black"
  //val START_UP_PIC = "CVExperimenterLogo.png"
  //val START_UP_PIC = "StartingPosition.png"
  //val START_UP_PIC = "BookChessboard.jpg"
  val START_UP_PIC = "algebraic_notation.gif"
  val USE_CHESSSCANNER_ON_VIDEOTAB = false
  val OUTPUT_PIECE_FEATURES = true

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
    scene = new Scene(1400,900) {
      fill = Black
      root = getLayout
    }
    tabManager.addImageTab("Logo", new Image(CVExperimenter.getClass.getClassLoader.getResourceAsStream(START_UP_PIC)))
  }

  /**
    * This is to insure the webcam is released.
    */
  override def stopApp(): Unit = {
    log.debug("Stopping")
    tabManager.stopVideoTab
    super.stopApp
  }
}
