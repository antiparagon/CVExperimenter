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
    output.append("AvgX").append(",").append("AvgY").append(",").append("AvgResp").append(",").append("Coord").append(",").append("Symbol").append(NL)

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
      case(coord, score) => output.append(score.avgX.toString).append(",").append(score.avgY.toString).append(",").append(score.avgResp.toString).append(",").append(coord).append(",").append(getSymbol(coord)).append(NL)
    }

    piecesFound
  }


  def getSymbol(coord: String): String = {
    coord match {
      case ("a8") => "r"
      case ("b8") => "n"
      case ("c8") => "b"
      case ("d8") => "q"
      case ("e8") => "k"
      case ("f8") => "b"
      case ("g8") => "n"
      case ("h8") => "r"
      case ("a7") => "p"
      case ("b7") => "p"
      case ("c7") => "p"
      case ("d7") => "p"
      case ("e7") => "p"
      case ("f7") => "p"
      case ("g7") => "p"
      case ("h7") => "p"

      case ("a1") => "R"
      case ("b1") => "N"
      case ("c1") => "B"
      case ("d1") => "Q"
      case ("e1") => "K"
      case ("f1") => "B"
      case ("g1") => "N"
      case ("h1") => "R"
      case ("a2") => "P"
      case ("b2") => "P"
      case ("c2") => "P"
      case ("d2") => "P"
      case ("e2") => "P"
      case ("f2") => "P"
      case ("g2") => "P"
      case ("h2") => "P"

      case (_) => "Unknown"
    }
  }

}
