package com.antiparagon.cvexperimenter.gui

import com.antiparagon.cvexperimenter.chessscanner.ChessScanner
import com.antiparagon.cvexperimenter.tools.ImageTools
import org.opencv.core.{Mat, Rect}

import scalafx.Includes._
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, ScrollPane, Tab}
import scalafx.scene.image.{Image, ImageView}
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
  val chessScanner = new ChessScanner

  val startButton = new Button {
    text = STEP1_TEXT
    style = BUTTON_STYLE
    onAction = handle {
      text.value match {
        case STEP1_TEXT => {
          println(STEP1_TEXT)
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
          println(STEP2_TEXT)
          val squares = chessScanner.findSquares()
          if(!squares.isEmpty) {
            chessScanner.drawSquaresFull()
            chessScanner.drawSquaresCoorFull()
            //ChessSquareFinder.drawGrid(board.get)
            //val squareH7 = chessScanner.chessboard.getSquare("h", 7)
            //val squareH6 = chessScanner.chessboard.getSquare("h", 6)
            //chessScanner.drawSquare(squareH7)
            //chessScanner.drawSquare(squareH6)
            imgView.setImage(ImageTools.convertCVtoFX(chessScanner.fullImage))
            text = STEP3_TEXT
          } else {
            println("Unable to find squares")
            text = "Done"
          }
        }
        case STEP3_TEXT => {
          println(STEP3_TEXT)
          val piecesFound = chessScanner.findPieces()
          println(s"Found $piecesFound pieces")
          text = STEP4_TEXT
        }
        case STEP4_TEXT => {
          println(STEP4_TEXT)
          val position = chessScanner.getFenPosition()
          println(s"$position")
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
