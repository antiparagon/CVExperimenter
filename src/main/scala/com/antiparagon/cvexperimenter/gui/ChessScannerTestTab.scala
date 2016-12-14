package com.antiparagon.cvexperimenter.gui

import com.antiparagon.cvexperimenter.chessscanner.{ChessSquareFinder, ChessboardFinder}
import com.antiparagon.cvexperimenter.tools.ImageTools
import com.typesafe.scalalogging.Logger
import org.opencv.core.{Mat, Scalar}
import org.opencv.imgproc.Imgproc
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
  var boardImage: Mat = null

  val startButton = new Button {
    text = STEP1_TEXT
    style = BUTTON_STYLE
    onAction = handle {
      text.value match {
        case STEP1_TEXT => {
          log.info(STEP1_TEXT)
          boardImage = ImageTools.convertFXtoCV(img)
          val boardRect = ChessboardFinder().findChessboard(boardImage)
          boardRect match {
            case Some(rect) => {
              text = STEP2_TEXT
              Imgproc.rectangle(boardImage, rect.tl, rect.br, new Scalar(0.0, 255.0, 0.0), 3)
              imgView.setImage(ImageTools.convertCVtoFX(boardImage))
            }
            case None => {
              println("Unable to find chessboard")
            }
          }
        }
        case STEP2_TEXT => {
          log.info(STEP2_TEXT)
          val squares = ChessSquareFinder().getChessboardSquares(boardImage)
          if(!squares.isEmpty) {
            log.info(s"Found ${squares.length} squares")
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
