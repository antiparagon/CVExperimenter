package com.antiparagon.cvexperimenter.chessscanner

import java.io.{File, PrintStream}

import org.opencv.core.{Core, Mat, Rect}
import org.opencv.imgcodecs.Imgcodecs

/**
  * This file is meant to collect the classifier data from the example chessboard images
  * saved in the 'CVExperimenter/images/Chess Scanner/Starting Position' folder.
  *
  * Created by wmckay on 4/5/17.
  */
object CreateXXXClassifierData {

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  val NL = System.lineSeparator
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


  /**
    * Main() that creates the data file.
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {

    val NL = System.lineSeparator
    val output = new PrintStream(new File("AllFastClassifierData.csv"))

    var chessPieceFinder = ChessPieceFinder(removeExt(CHESS_BOARD_NEW))
    doCHESS_BOARD_NEW(chessPieceFinder: ChessPieceFinder)

    val numKeyPoints = chessPieceFinder.classifier.numKeyPoints
    // Output header row here because the number of key points points is now known from using ChessPieceFinder.
    outputHeaderRow(numKeyPoints, output)

    outputScores(chessPieceFinder, numKeyPoints, output)

    chessPieceFinder = ChessPieceFinder(removeExt(CHESS_KID_MODIFIED))
    doCHESS_KID_MODIFIED(chessPieceFinder)
    outputScores(chessPieceFinder, numKeyPoints, output)

    chessPieceFinder = ChessPieceFinder(removeExt(DIAGRAM_OF_CHESS_BOARD_SETUP_MODIFIED))
    doDIAGRAM_OF_CHESS_BOARD_SETUP_MODIFIED(chessPieceFinder)
    outputScores(chessPieceFinder, numKeyPoints, output)

    chessPieceFinder = ChessPieceFinder(removeExt(KID_CHESS_SETUP_BOARD))
    doKID_CHESS_SETUP_BOARD(chessPieceFinder)
    outputScores(chessPieceFinder, numKeyPoints, output)

    chessPieceFinder = ChessPieceFinder(removeExt(NUMBER))
    doNUMBER(chessPieceFinder)
    outputScores(chessPieceFinder, numKeyPoints, output)

    chessPieceFinder = ChessPieceFinder(removeExt(POSITION))
    doPOSITION(chessPieceFinder)
    outputScores(chessPieceFinder, numKeyPoints, output)

    chessPieceFinder = ChessPieceFinder(removeExt(STAGRAM_MODIFIED))
    doSTAGRAM_MODIFIED(chessPieceFinder)
    outputScores(chessPieceFinder, numKeyPoints, output)

    chessPieceFinder = ChessPieceFinder(removeExt(STARTING_POSITION))
    doSTARTING_POSITION(chessPieceFinder)
    outputScores(chessPieceFinder, numKeyPoints, output)

    chessPieceFinder = ChessPieceFinder(removeExt(VP_BLACKARRAY_MODIFIED))
    doVP_BLACKARRAY_MODIFIED(chessPieceFinder)
    outputScores(chessPieceFinder, numKeyPoints, output)

    output.close()
  }

  /**
    * Writes the CSV header row to the output.
    *
    * @param numKeyPoints number of key points to output
    * @param output PrintStream to output to
    */
  def outputHeaderRow(numKeyPoints: Int, output: PrintStream): Unit = {
    output.append("AvgKeyPointX").append(",").append("AvgKeyPointY").append(",").append("AvgKeyPointResp").append(",").append("Symbol")
    // Add key point headers
    for(i <- 1 to numKeyPoints) {
      output.append(",").append(s"KeyPoint${i}X").append(",").append(s"KeyPoint${i}Y").append(",").append(s"KeyPoint${i}Resp")
    }
    output.append(NL)
  }

  /**
    * Writes the scores from the ChessPieceFinder to the output.
    *
    * @param finder ChessPieceFinder with the scores to output
    * @param numKeyPoints number of scores to output
    * @param output PrintStream to output to
    */
  def outputScores(finder: ChessPieceFinder, numKeyPoints: Int, output: PrintStream): Unit = {
    val NL = System.lineSeparator
    finder.classifier.scores.foreach {
      case(coord, score) => {
        output.append(score.avgX.toString).append(",").append(score.avgY.toString).append(",").append(score.avgResp.toString).append(",").append(getSymbol(coord))
          // Add keypoints points
          for(i <- 0 until numKeyPoints) {
            output.append(",").append(s"${score.keyPoints(i).pt.x}").append(",").append(s"${score.keyPoints(i).pt.y}").append(",").append(s"${score.keyPoints(i).response}")
          }
          output.append(NL)
      }
    }
  }


  def doCHESS_BOARD_NEW(chessPieceFinder: ChessPieceFinder): Unit = {
    val img = Imgcodecs.imread(IMG_FOLDER + CHESS_BOARD_NEW)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    assert(rect.get == CHESS_BOARD_NEW_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    assert(squares.length == 64)
    val chessboard = Chessboard.create(squares)
    val pieces = chessPieceFinder.findChessPieces(chessboard, boardImage)
    assert(pieces == 32)
  }


  def doCHESS_KID_MODIFIED(chessPieceFinder: ChessPieceFinder): Unit = {
    val img = Imgcodecs.imread(IMG_FOLDER + CHESS_KID_MODIFIED)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    assert(rect.get == CHESS_KID_MODIFIED_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    assert(squares.length == 64)
    val chessboard = Chessboard.create(squares)
    val pieces = chessPieceFinder.findChessPieces(chessboard, boardImage)
    assert(pieces == 32)
  }


  def doDIAGRAM_OF_CHESS_BOARD_SETUP_MODIFIED(chessPieceFinder: ChessPieceFinder): Unit = {
    val img = Imgcodecs.imread(IMG_FOLDER + DIAGRAM_OF_CHESS_BOARD_SETUP_MODIFIED)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    assert(rect.get == DIAGRAM_OF_CHESS_BOARD_SETUP_MODIFIED_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    assert(squares.length == 64)
    val chessboard = Chessboard.create(squares)
    val pieces = chessPieceFinder.findChessPieces(chessboard, boardImage)
    assert(pieces == 32)
  }


  def doKID_CHESS_SETUP_BOARD(chessPieceFinder: ChessPieceFinder): Unit = {
    val img = Imgcodecs.imread(IMG_FOLDER + KID_CHESS_SETUP_BOARD)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    assert(rect.get == KID_CHESS_SETUP_BOARD_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    assert(squares.length == 64)
    val chessboard = Chessboard.create(squares)
    val pieces = chessPieceFinder.findChessPieces(chessboard, boardImage)
    assert(pieces == 32)
  }


  def doNUMBER(chessPieceFinder: ChessPieceFinder): Unit = {
    val img = Imgcodecs.imread(IMG_FOLDER + NUMBER)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    assert(rect.get == NUMBER_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    assert(squares.length == 64)
    val chessboard = Chessboard.create(squares)
    val pieces = chessPieceFinder.findChessPieces(chessboard, boardImage)
    assert(pieces == 32)
  }


  def doPOSITION(chessPieceFinder: ChessPieceFinder): Unit = {
    val img = Imgcodecs.imread(IMG_FOLDER + POSITION)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    assert(rect.get == POSITION_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    assert(squares.length == 64)
    val chessboard = Chessboard.create(squares)
    val pieces = chessPieceFinder.findChessPieces(chessboard, boardImage)
    assert(pieces == 32)
  }


  def doSTAGRAM_MODIFIED(chessPieceFinder: ChessPieceFinder): Unit = {
    val img = Imgcodecs.imread(IMG_FOLDER + STAGRAM_MODIFIED)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    assert(rect.get == STAGRAM_MODIFIED_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    assert(squares.length == 64)
    val chessboard = Chessboard.create(squares)
    val pieces = chessPieceFinder.findChessPieces(chessboard, boardImage)
    assert(pieces == 32)
  }


  def doSTARTING_POSITION(chessPieceFinder: ChessPieceFinder): Unit = {
    val img = Imgcodecs.imread(IMG_FOLDER + STARTING_POSITION)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    assert(rect.get == STARTING_POSITION_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    assert(squares.length == 64)
    val chessboard = Chessboard.create(squares)
    val pieces = chessPieceFinder.findChessPieces(chessboard, boardImage)
    assert(pieces == 32)
  }


  def doVP_BLACKARRAY_MODIFIED(chessPieceFinder: ChessPieceFinder): Unit = {
    val img = Imgcodecs.imread(IMG_FOLDER + VP_BLACKARRAY_MODIFIED)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    assert(rect.get == VP_BLACKARRAY_MODIFIED_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    assert(squares.length == 64)
    val chessboard = Chessboard.create(squares)
    val pieces = chessPieceFinder.findChessPieces(chessboard, boardImage)
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


//      case ("a8") => "R"
//      case ("b8") => "N"
//      case ("c8") => "B"
//      case ("d8") => "Q"
//      case ("e8") => "K"
//      case ("f8") => "B"
//      case ("g8") => "N"
//      case ("h8") => "R"
//      case ("a7") => "P"
//      case ("b7") => "P"
//      case ("c7") => "P"
//      case ("d7") => "P"
//      case ("e7") => "P"
//      case ("f7") => "P"
//      case ("g7") => "P"
//      case ("h7") => "P"

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
