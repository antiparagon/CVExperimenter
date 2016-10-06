package com.antiparagon.cvexperimenter.chessscanner

import java.io.{File, PrintStream}

import org.opencv.core.Mat


/**
  * Created by wmckay on 6/12/16.
  */
object ChessPieceFinder {

  def findChessPieces(chessboard: Chessboard, boardImg: Mat): Int = {

    var piecesFound = 0

    if(chessboard == null || boardImg == null || boardImg.empty()) {
      return 0
    }

    val NL = System.lineSeparator()
    val output = new PrintStream(new File("foundpieces.csv"))
    output.append("Square").append(",").append("AvgX").append(",").append("AvgY").append(",").append("AvgResp").append(NL)

    val classifier = new ChessPieceClassifierFast();

    chessboard.getSquares().foreach(square => {
      val squareImg = new Mat(boardImg, square.rect)

      val (col, row) = chessboard.translateMatrixCoor(square.row, square.column)
      val coorStr = col + row.toString

      classifier.classifyPiece(squareImg, coorStr) match {
        case Some(piece) => {
          square.piece = piece
          piecesFound += 1
        }
        case None =>
      }
    })

    classifier.scores.foreach {
      case(coord, score) => output.append(coord).append(",").append(score.avgX.toString).append(",").append(score.avgY.toString).append(",").append(score.avgResp.toString).append(NL)
    }

    piecesFound
  }

}
