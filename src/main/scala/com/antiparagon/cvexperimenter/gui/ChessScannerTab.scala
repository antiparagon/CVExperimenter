package com.antiparagon.cvexperimenter.gui

import com.antiparagon.cvexperimenter.chessscanner.{ChessScanner, ChessSquareFinder, Chessboard}
import com.antiparagon.cvexperimenter.tools.ImageTools
import org.opencv.core.{Mat, Rect, Scalar}
import org.opencv.imgproc.Imgproc

import scala.collection.mutable.ArrayBuffer
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
  val chessScanner = new ChessScanner

  val startButton = new Button {
    var boardImg: Option[Mat] = None
    var squares: ArrayBuffer[Rect] = ArrayBuffer.empty[Rect]
    var board: Option[Chessboard] = None
    text = STEP1_TEXT
    style = BUTTON_STYLE
    onAction = handle {
      text.value match {
        case STEP1_TEXT => {
          println(STEP1_TEXT)
          boardImg = chessScanner.findChessboard(ImageTools.convertFXtoCV(img))
          if(!boardImg.isEmpty) {
            text = STEP2_TEXT
            imgView.setImage(ImageTools.convertCVtoFX(boardImg.get))
          } else {
            println("Unable to find chessboard")
          }
        }
        case STEP2_TEXT => {
          println(STEP2_TEXT)
          squares = chessScanner.findSquares()
          if(!squares.isEmpty) {
            ChessSquareFinder.drawSquares(boardImg.get, squares)
            //ChessSquareFinder.drawGrid(board.get)
            imgView.setImage(ImageTools.convertCVtoFX(boardImg.get))
            text = STEP3_TEXT
          } else {
            println("Unable to find squares")
            text = "Done"
          }
        }
        case STEP3_TEXT => {
          println(STEP3_TEXT)
          board = chessScanner.findPieces()
          if(!board.isEmpty) {
            text = STEP4_TEXT
          } else {
            println("Unable to find pieces")
            text = "Done"
          }
        }
        case STEP4_TEXT => {
          println(STEP4_TEXT)
          val position = chessScanner.getFenPosition()
          if(!position.isEmpty) {
            println(position.get)
            text = "Done"
          } else {
            println("Unable to get position")
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
