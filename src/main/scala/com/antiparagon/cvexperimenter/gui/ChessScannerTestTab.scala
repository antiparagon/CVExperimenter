package com.antiparagon.cvexperimenter.gui

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scalafx.Includes._
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, ScrollPane, Tab}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{HBox, VBox}

/**
  * GUI element for ChessScanner test code.
  *
  * Created by wmckay on 10/11/16.
  */
class ChessScannerTestTab(val img : Image) extends Tab with ExperimenterTab {

  val log = Logger(LoggerFactory.getLogger("ChessScannerTestTab"))

  val STEP1_TEXT = "Find Chessboard"
  val STEP2_TEXT = "Find Squares"

  val imgView =  new ImageView(img)

  val startButton = new Button {
    text = STEP1_TEXT
    style = BUTTON_STYLE
    onAction = handle {
      text.value match {
        case STEP1_TEXT => {
          log.info(STEP1_TEXT)
          text = STEP2_TEXT
        }
        case STEP2_TEXT => {
          log.info(STEP2_TEXT)
          text = "Done"
        }

        case _ =>
      }
    }
  }

  content = new VBox {
    padding = Insets(20)
    style = BACKGROUND_STYLE
    alignment = Pos.Center
    children = Seq(
      new HBox {
        alignment = Pos.Center
        children = new ScrollPane {
          style = BACKGROUND_STYLE
          content = new VBox {
            padding = Insets(20)
            style = BACKGROUND_STYLE
            spacing = 5
            children = Seq(
              imgView,
              new HBox {
                alignment = Pos.Center
                children = startButton
              }
            )
          }
        }
      }
    )
  }

  override def getImg() : Image = return imgView.getImage

  override def getTabText(): String = text.value

}
