package com.antiparagon.cvexperimenter.gui

import com.antiparagon.cvexperimenter.chessscanner.ChessScanner
import com.antiparagon.cvexperimenter.tools.ImageTools
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scalafx.Includes._
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, ScrollPane, Tab}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{HBox, VBox}

/**
  * GUI element for ChessScanner code.
  *
  * Created by wmckay on 10/11/16.
  */
class ChessScannerTestTab(val img : Image) extends Tab with ExperimenterTab {

  val log = Logger(LoggerFactory.getLogger("ChessScannerTestTab"))

  val STEP1_TEXT = "Find Chessboard"
  val STEP2_TEXT = "Find Squares"

  val imgView =  new ImageView(img)
  val chessScanner = new ChessScanner // TODO: Remove and use ChessboardTestFinder

  val startButton = new Button {
    text = "Test"
    style = BUTTON_STYLE
    onAction = handle {
      text.value match {
        case STEP1_TEXT => {
          log.info(STEP1_TEXT)
          val boardImg = chessScanner.findChessboard(ImageTools.convertFXtoCV(img))
          boardImg match {
            case Some(boardImg) => {
              text = STEP2_TEXT
              imgView.setImage(ImageTools.convertCVtoFX(boardImg))
            }
            case None => println("Unable to find chessboard")
          }
        }
        case STEP2_TEXT => {
          log.info(STEP2_TEXT)
          val squares = chessScanner.findSquares()
          if(!squares.isEmpty) {
            chessScanner.drawSquaresFull()
            chessScanner.drawSquaresCoorFull()
            imgView.setImage(ImageTools.convertCVtoFX(chessScanner.fullImage))
            text = "Done"
          } else {
            println("Unable to find squares")
            text = "Done"
          }
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
