package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.{Mat, Rect}

/**
  * Factory methods to create ChessSquareFinder.
  *
  * Created by wmckay on 1/2/17.
  */
object ChessPieceFinder {

  def apply(): ChessPieceFinder = {
    new ChessPieceFinder
  }

  def apply(debugImagePrefix: String): ChessPieceFinder = {
    val chessPieceFinder = new ChessPieceFinder
    chessPieceFinder.outputDebugImgs = true
    chessPieceFinder.debugImgPrefix = debugImagePrefix
    chessPieceFinder
  }
}

/**
  * Created by wmckay on 6/12/16.
  */
class ChessPieceFinder {

  /*
    For debugging of the algorithm. Outputs intermediate stage images.
   */
  var outputDebugImgs = false
  // Prefix for debug images
  var debugImgPrefix = "ChessboardFinder_"

  def outputTrainingData(chessboard: Chessboard, boardImg: Mat): Int = {

    var piecesFound = 0

    if(chessboard == null || boardImg == null || boardImg.empty()) {
      return 0
    }

    //val NL = System.lineSeparator()
    //val output = new PrintStream(new File("foundpieces.csv"))
    //output.append("AvgX").append(",").append("AvgY").append(",").append("AvgResp").append(",").append("Coord").append(",").append("Symbol").append(NL)

    val classifier = ChessPieceClassifierFast(debugImgPrefix)
    val emptyRect = new Rect

    chessboard.getSquares().foreach(square => {

      if(square.rect != emptyRect) {
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
      }
    })

    val scores = scala.collection.mutable.Map[String, FeatureScoreFast]()
    var halfBP1AvgX = 0.0
    var halfBP1AvgY = 0.0
    var halfBP1AvgResp = 0.0
    var halfBP1Count = 0.0
    var halfBP2AvgX = 0.0
    var halfBP2AvgY = 0.0
    var halfBP2AvgResp = 0.0
    var halfBP2Count = 0.0

    var halfWP1AvgX = 0.0
    var halfWP1AvgY = 0.0
    var halfWP1AvgResp = 0.0
    var halfWP1Count = 0.0
    var halfWP2AvgX = 0.0
    var halfWP2AvgY = 0.0
    var halfWP2AvgResp = 0.0
    var halfWP2Count = 0.0

    classifier.scores.foreach {
      case (coord, score) => {
        coord match {
          case ("a7") | ("b7") | ("c7") | ("d7") => {
            halfBP1AvgX += score.avgX
            halfBP1AvgY += score.avgY
            halfBP1AvgResp += score.avgResp
            halfBP1Count += 1
          }
          case ("e7") | ("f7") | ("g7") | ("h7") => {
            halfBP2AvgX += score.avgX
            halfBP2AvgY += score.avgY
            halfBP2AvgResp += score.avgResp
            halfBP2Count += 1
          }
          case ("a2") | ("b2") | ("c2") | ("d2") => {
            halfWP1AvgX += score.avgX
            halfWP1AvgY += score.avgY
            halfWP1AvgResp += score.avgResp
            halfWP1Count += 1
          }
          case ("e2") | ("f2") | ("g2") | ("h2") => {
            halfWP2AvgX += score.avgX
            halfWP2AvgY += score.avgY
            halfWP2AvgResp += score.avgResp
            halfWP2Count += 1
          }

          case (_) => scores += (coord -> score)
        }
      }
    }

    halfBP1AvgX = halfBP1AvgX / halfBP1Count
    halfBP1AvgY = halfBP1AvgY / halfBP1Count
    halfBP1AvgResp = halfBP1AvgResp / halfBP1Count

    halfBP2AvgX = halfBP2AvgX / halfBP2Count
    halfBP2AvgY = halfBP2AvgY / halfBP2Count
    halfBP2AvgResp = halfBP2AvgResp / halfBP2Count

    halfWP1AvgX = halfWP1AvgX / halfWP1Count
    halfWP1AvgY = halfWP1AvgY / halfWP1Count
    halfWP1AvgResp = halfWP1AvgResp / halfWP1Count

    halfWP2AvgX = halfWP2AvgX / halfWP2Count
    halfWP2AvgY = halfWP2AvgY / halfWP2Count
    halfWP2AvgResp = halfWP2AvgResp / halfWP2Count

    scores += ("a7" -> FeatureScoreFast(halfBP1AvgX, halfBP1AvgY, halfBP1AvgResp))
    scores += ("e7" -> FeatureScoreFast(halfBP2AvgX, halfBP2AvgY, halfBP2AvgResp))

    scores += ("a2" -> FeatureScoreFast(halfWP1AvgX, halfWP1AvgY, halfWP1AvgResp))
    scores += ("e2" -> FeatureScoreFast(halfWP2AvgX, halfWP2AvgY, halfWP2AvgResp))

    //scores.foreach {
    //  case(coord, score) => output.append(score.avgX.toString).append(",").append(score.avgY.toString).append(",").append(score.avgResp.toString)
    //    .append(",").append(coord).append(",").append(getSymbol(coord)).append(NL)
    //}

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

  def findChessPieces(chessboard: Chessboard, boardImg: Mat): Int = {

    var piecesFound = 0

    if(chessboard == null || boardImg == null || boardImg.empty()) {
      return 0
    }

    //val NL = System.lineSeparator()
    //val output = new PrintStream(new File("foundpieces.csv"))
    //output.append("AvgX").append(",").append("AvgY").append(",").append("AvgResp").append(",").append("Coord").append(",").append("Symbol").append(NL)

    val classifier = ChessPieceClassifierFast(debugImgPrefix)
    val emptyRect = new Rect

    chessboard.getSquares().foreach(square => {

      if(square.rect != emptyRect) {
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
      }
    })

    val scores = scala.collection.mutable.Map[String, FeatureScoreFast]()
    var halfBP1AvgX = 0.0
    var halfBP1AvgY = 0.0
    var halfBP1AvgResp = 0.0
    var halfBP1Count = 0.0
    var halfBP2AvgX = 0.0
    var halfBP2AvgY = 0.0
    var halfBP2AvgResp = 0.0
    var halfBP2Count = 0.0

    var halfWP1AvgX = 0.0
    var halfWP1AvgY = 0.0
    var halfWP1AvgResp = 0.0
    var halfWP1Count = 0.0
    var halfWP2AvgX = 0.0
    var halfWP2AvgY = 0.0
    var halfWP2AvgResp = 0.0
    var halfWP2Count = 0.0

    classifier.scores.foreach {
      case (coord, score) => {
        coord match {
          case ("a7") | ("b7") | ("c7") | ("d7") => {
            halfBP1AvgX += score.avgX
            halfBP1AvgY += score.avgY
            halfBP1AvgResp += score.avgResp
            halfBP1Count += 1
          }
          case ("e7") | ("f7") | ("g7") | ("h7") => {
            halfBP2AvgX += score.avgX
            halfBP2AvgY += score.avgY
            halfBP2AvgResp += score.avgResp
            halfBP2Count += 1
          }
          case ("a2") | ("b2") | ("c2") | ("d2") => {
            halfWP1AvgX += score.avgX
            halfWP1AvgY += score.avgY
            halfWP1AvgResp += score.avgResp
            halfWP1Count += 1
          }
          case ("e2") | ("f2") | ("g2") | ("h2") => {
            halfWP2AvgX += score.avgX
            halfWP2AvgY += score.avgY
            halfWP2AvgResp += score.avgResp
            halfWP2Count += 1
          }

          case (_) => scores += (coord -> score)
        }
      }
    }

    halfBP1AvgX = halfBP1AvgX / halfBP1Count
    halfBP1AvgY = halfBP1AvgY / halfBP1Count
    halfBP1AvgResp = halfBP1AvgResp / halfBP1Count

    halfBP2AvgX = halfBP2AvgX / halfBP2Count
    halfBP2AvgY = halfBP2AvgY / halfBP2Count
    halfBP2AvgResp = halfBP2AvgResp / halfBP2Count

    halfWP1AvgX = halfWP1AvgX / halfWP1Count
    halfWP1AvgY = halfWP1AvgY / halfWP1Count
    halfWP1AvgResp = halfWP1AvgResp / halfWP1Count

    halfWP2AvgX = halfWP2AvgX / halfWP2Count
    halfWP2AvgY = halfWP2AvgY / halfWP2Count
    halfWP2AvgResp = halfWP2AvgResp / halfWP2Count

    scores += ("a7" -> FeatureScoreFast(halfBP1AvgX, halfBP1AvgY, halfBP1AvgResp))
    scores += ("e7" -> FeatureScoreFast(halfBP2AvgX, halfBP2AvgY, halfBP2AvgResp))

    scores += ("a2" -> FeatureScoreFast(halfWP1AvgX, halfWP1AvgY, halfWP1AvgResp))
    scores += ("e2" -> FeatureScoreFast(halfWP2AvgX, halfWP2AvgY, halfWP2AvgResp))

    //scores.foreach {
    //  case(coord, score) => output.append(score.avgX.toString).append(",").append(score.avgY.toString).append(",").append(score.avgResp.toString)
    //    .append(",").append(coord).append(",").append(getSymbol(coord)).append(NL)
    //}

    piecesFound
  }
}
