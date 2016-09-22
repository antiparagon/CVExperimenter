package com.antiparagon.cvexperimenter.chessscanner

import java.io.PrintStream

import org.opencv.core._
import org.opencv.features2d.FeatureDetector

/**
  * Created by wmckay on 6/12/16.
  */
object ChessPieceFinder {

  def findChessPieces(chessboard: Chessboard, boardImg: Mat): Int = {

    import scala.collection.JavaConverters._

    var piecesFound = 0
    val NL = System.getProperty("line.separator")
    var output: PrintStream = null

    if(chessboard == null || boardImg == null || boardImg.empty()) {
      return 0
    }

//    if(CVExperimenter.OUTPUT_PIECE_FEATURES) {
//      output = new PrintStream(new File("foundpieces.csv"))
//      output.append("Square").append(",").append("X").append(",").append("Y").append(",").append("Response").append(",").append("Piece").append(NL)
//    }

    val features = FeatureDetector.create(FeatureDetector.FAST)

    chessboard.getSquares().foreach(square => {
      val squareImg = new Mat(boardImg, square.rect)

      val keyPointsMat = new MatOfKeyPoint()
      features.detect(squareImg, keyPointsMat)

      val (col, row) = chessboard.translateMatrixCoor(square.row, square.column)
      val coorStr = col + row.toString

      ChessPieceClassifier.classifyPiece(squareImg, keyPointsMat) match {
        case Some(piece) => {
          square.piece = piece
          piecesFound += 1
        }
        case None =>
      }
    })

//    if(CVExperimenter.OUTPUT_PIECE_FEATURES && output != null) {
//      output.close()
//    }

    piecesFound
  }

}
