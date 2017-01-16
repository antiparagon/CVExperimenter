package com.antiparagon.cvexperimenter.chessscanner

import java.io.File

import org.opencv.core.{Core, Mat, Rect}
import org.opencv.imgcodecs.Imgcodecs

/**
  * This file is meant to collect the classifer training data from the example chessboard images
  * saved in the 'CVExperimenter/images/Chess Scanner/Starting Position' folder.
  *
  * Created by wmckay on 1/16/17.
  */
object ChessPieceFastClassifierTrainingData {

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
