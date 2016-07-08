package com.antiparagon.cvexperimenter.gui

import scalafx.Includes._
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, ScrollPane, Tab}
import scalafx.scene.image.{Image, ImageView, WritableImage}
import scalafx.scene.layout.{HBox, VBox}

/**
  * GUI element for ChessScanner code.
  *
  * Created by wmckay on 5/5/16.
  */
class ChessScannerTab(val img : Image) extends Tab with ExperimenterTab {

  val STEP1_TEXT = "Find Chessboard"
  val STEP2_TEXT = "Find Squares"
  val STEP3_TEXT = "Find Pieces"
  val STEP4_TEXT = "Get Position"

  val imgView =  new ImageView(img)

  val startButton = new Button {
    text = STEP1_TEXT
    style = BUTTON_STYLE
    onAction = handle {
      text.value match {
        case STEP1_TEXT => {
          println(STEP1_TEXT)
          text = STEP2_TEXT
        }
        case STEP2_TEXT => {
          println(STEP2_TEXT)
          text = STEP3_TEXT
        }
        case STEP3_TEXT => {
          println(STEP3_TEXT)
          text = STEP4_TEXT
        }
        case STEP4_TEXT => {
          println(STEP4_TEXT)
          text = "Done"
        }
        case _ => {}
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

  override def getImg() : Image = return img

  override def getTabText(): String = text.value

}
