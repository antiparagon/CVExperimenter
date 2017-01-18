package com.antiparagon.cvexperimenter.chessscanner

import java.io.{File, PrintStream}

import org.opencv.core.{Core, Mat, Rect}
import org.opencv.imgcodecs.Imgcodecs

/**
  * This file is meant to collect the classifer training data from the example chessboard images
  * saved in the 'CVExperimenter/images/Chess Scanner/Starting Position' folder.
  *
  * Created by wmckay on 1/16/17.
  */
object CreateFastClassifierTrainingData {

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  val IMG_FOLDER = "images/Chess Scanner/Starting Position/"
  val OUTPUT_FOLDER = "Debug Images/"

  val CHESS_BOARD_NEW = "chess-board-new.jpg"
  val CHESS_BOARD_NEW_RECT = new Rect(24, 1, 374, 371)

  val CHESS_KID_MODIFIED = "chesskid_modified.png"
  val CHESS_KID_MODIFIED_RECT = new Rect(42, 10, 632, 632)

  val DIAGRAM_OF_CHESS_BOARD_SETUP_MODIFIED = "diagram-of-chess-board-setup_modified.png"
  val DIAGRAM_OF_CHESS_BOARD_SETUP_MODIFIED_RECT = new Rect(16, 11, 266, 274)

  val KID_CHESS_SETUP_BOARD = "kid-chess-setup-board.png"
  val KID_CHESS_SETUP_BOARD_RECT = new Rect(18, 17, 322, 323)

  val NUMBER = "number.png"
  val NUMBER_RECT = new Rect(7, 88, 448, 450)

  val POSITION = "position.png"
  val POSITION_RECT = new Rect(16, 0, 262, 263)

  val STAGRAM_MODIFIED = "stagram_modified.png"
  val STAGRAM_MODIFIED_RECT = new Rect(15, 13, 298, 300)

  val STARTING_POSITION = "StartingPosition.png"
  val STARTING_POSITION_RECT = new Rect(95, 49, 323, 324)

  val VP_BLACKARRAY_MODIFIED = "VP-Blackarray_modified.png"
  val VP_BLACKARRAY_MODIFIED_RECT = new Rect(6, 8, 280, 279)


  def main(args: Array[String]): Unit = {
    val NL = System.lineSeparator()
    val output = new PrintStream(new File("foundpieces.csv"))
    output.append("AvgX").append(",").append("AvgY").append(",").append("AvgResp").append(",").append("Coord").append(",").append("Symbol").append(NL)

    /*
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
    */
  }


  def doCHESS_BOARD_NEW(): Unit = {
    val img = Imgcodecs.imread(IMG_FOLDER + CHESS_BOARD_NEW)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    assert(rect.get == CHESS_BOARD_NEW_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    assert(squares.length == 64)
    val chessboard = Chessboard.create(squares)
    val pieces = ChessPieceFinder(removeExt(CHESS_BOARD_NEW)).findChessPieces(chessboard, boardImage)
    assert(pieces == 32)
  }


  def doCHESS_KID_MODIFIED(): Unit = {
    val img = Imgcodecs.imread(IMG_FOLDER + CHESS_KID_MODIFIED)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    assert(rect.get == CHESS_KID_MODIFIED_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    assert(squares.length == 64)
    val chessboard = Chessboard.create(squares)
    val pieces = ChessPieceFinder(removeExt(CHESS_KID_MODIFIED)).findChessPieces(chessboard, boardImage)
    assert(pieces == 32)
  }


  def doDIAGRAM_OF_CHESS_BOARD_SETUP_MODIFIED(): Unit = {
    val img = Imgcodecs.imread(IMG_FOLDER + DIAGRAM_OF_CHESS_BOARD_SETUP_MODIFIED)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    assert(rect.get == DIAGRAM_OF_CHESS_BOARD_SETUP_MODIFIED_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    assert(squares.length == 64)
    val chessboard = Chessboard.create(squares)
    val pieces = ChessPieceFinder(removeExt(DIAGRAM_OF_CHESS_BOARD_SETUP_MODIFIED)).findChessPieces(chessboard, boardImage)
    assert(pieces == 32)
  }


  def doKID_CHESS_SETUP_BOARD(): Unit = {
    val img = Imgcodecs.imread(IMG_FOLDER + KID_CHESS_SETUP_BOARD)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    assert(rect.get == KID_CHESS_SETUP_BOARD_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    assert(squares.length == 64)
    val chessboard = Chessboard.create(squares)
    val pieces = ChessPieceFinder(removeExt(KID_CHESS_SETUP_BOARD)).findChessPieces(chessboard, boardImage)
    assert(pieces == 32)
  }


  def doNUMBER(): Unit = {
    val img = Imgcodecs.imread(IMG_FOLDER + NUMBER)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    assert(rect.get == NUMBER_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    assert(squares.length == 64)
    val chessboard = Chessboard.create(squares)
    val pieces = ChessPieceFinder(removeExt(NUMBER)).findChessPieces(chessboard, boardImage)
    assert(pieces == 32)
  }


  def doPOSITION(): Unit = {
    val img = Imgcodecs.imread(IMG_FOLDER + POSITION)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    assert(rect.get == POSITION_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    assert(squares.length == 64)
    val chessboard = Chessboard.create(squares)
    val pieces = ChessPieceFinder(removeExt(POSITION)).findChessPieces(chessboard, boardImage)
    assert(pieces == 32)
  }


  def doSTAGRAM_MODIFIED(): Unit = {
    val img = Imgcodecs.imread(IMG_FOLDER + STAGRAM_MODIFIED)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    assert(rect.get == STAGRAM_MODIFIED_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    assert(squares.length == 64)
    val chessboard = Chessboard.create(squares)
    val pieces = ChessPieceFinder(removeExt(STAGRAM_MODIFIED)).findChessPieces(chessboard, boardImage)
    assert(pieces == 32)
  }


  def doSTARTING_POSITION(): Unit = {
    val img = Imgcodecs.imread(IMG_FOLDER + STARTING_POSITION)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    assert(rect.get == STARTING_POSITION_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    assert(squares.length == 64)
    val chessboard = Chessboard.create(squares)
    val pieces = ChessPieceFinder(removeExt(STARTING_POSITION)).findChessPieces(chessboard, boardImage)
    assert(pieces == 32)
  }


  def doVP_BLACKARRAY_MODIFIED(): Unit = {
    val img = Imgcodecs.imread(IMG_FOLDER + VP_BLACKARRAY_MODIFIED)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    assert(rect.get == VP_BLACKARRAY_MODIFIED_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    assert(squares.length == 64)
    val chessboard = Chessboard.create(squares)
    val pieces = ChessPieceFinder(removeExt(VP_BLACKARRAY_MODIFIED)).findChessPieces(chessboard, boardImage)
    assert(pieces == 32)
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

  def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }

  def removeExt(filename: String): String = {
    if(filename.contains(".")) filename.substring(0, filename.lastIndexOf('.'))
    else filename
  }

}
