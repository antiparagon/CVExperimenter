package com.antiparagon.cvexperimenter.chessscanner

import java.io.File

import org.opencv.core.{Core, Mat, Rect}
import org.opencv.imgcodecs.Imgcodecs
import org.scalatest._

/**
  * This test file is meant to test all the example chessboard images saved in the
  * 'CVExperimenter\images\Chess Scanner\Starting Position' folder.
  *
  * Created by wmckay on 12/31/16.
  */
class ChessPieceFinderFailuresTester extends FlatSpec with Matchers {

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  val IMG_FOLDER = "images/Chess Scanner/Starting Position/"
  val OUTPUT_FOLDER = "Debug Images/"

  val ALGEBRAIC_NOTATION = "algebraic_notation.png"
  val ALGEBRAIC_NOTATION_RECT = new Rect(12, 11, 251, 255)

  val BEGINN1 = "Beginn1.png"
  val BEGINN1_RECT = new Rect(13, 13, 223, 223)

  val BOARD_SETUP = "BoardSetup.jpg"
  val BOARD_SETUP_RECT = new Rect(13, 13, 223, 223)

  val CHESS_BOARD_SET_UP_MODIFIED = "chess_board_set_up_modified.jpg"
  val CHESS_BOARD_SET_UP_MODIFIED_RECT = new Rect(9, 9, 350, 350)

  val CHESS_MODIFIED = "chess_modified.png"
  val CHESS_MODIFIED_RECT = new Rect(2, 0, 241, 245)

  val CHESS_BOARD_2 = "ChessBoard2.jpg"
  val CHESS_BOARD_2_RECT = new Rect(49, 50, 630, 635)

  val FENBOARD = "fenboard.png"
  val FENBOARD_RECT = new Rect(14, 0, 242, 244)

  val FRITZBSMALL = "fritzbsmall.png"
  val FRITZBSMALL_RECT = new Rect(0, 0, 200, 202)

  val PURPLE = "purple.png"
  val PURPLE_RECT = new Rect(2, 2, 466, 260)


  "ChessPieceFinder" should "return 32 when given image " + ALGEBRAIC_NOTATION in {
    val img = Imgcodecs.imread(IMG_FOLDER + ALGEBRAIC_NOTATION)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (ALGEBRAIC_NOTATION_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    squares.length should be (64)
    val chessboard = Chessboard.create(squares)
    val pieces = ChessPieceFinder().findChessPieces(chessboard, boardImage)
    pieces should be (32)
  }


  //  "ChessPieceFinder" should "rreturn 32 when given image "" + BEGINN1 in {
  //    val img = Imgcodecs.imread(IMG_FOLDER + BEGINN1)
  //    assert(!img.empty())
  //    val rect = ChessboardFinder().findChessboard(img)
  //    //val rect = ChessboardFinder().findChessboard(img)
  //    assert(rect.isDefined)
  //    rect.get should be (BEGINN1_RECT)
  //    val boardImage = new Mat(img, rect.get)
  //    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
  //    squares.length should be (64)
  //    val chessboard = Chessboard.create(squares)
  //    val pieces = ChessPieceFinder().findChessPieces(chessboard, boardImage)
  //    pieces should be (32)
  //  }


  "ChessPieceFinder" should "return 32 when given image " + BOARD_SETUP in {
    val img = Imgcodecs.imread(IMG_FOLDER + BOARD_SETUP)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (BOARD_SETUP_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    squares.length should be (64)
    val chessboard = Chessboard.create(squares)
    val pieces = ChessPieceFinder().findChessPieces(chessboard, boardImage)
    pieces should be (32)
  }


  "ChessPieceFinder" should "return 32 when given image " + CHESS_BOARD_SET_UP_MODIFIED in {
    val img = Imgcodecs.imread(IMG_FOLDER + CHESS_BOARD_SET_UP_MODIFIED)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (CHESS_BOARD_SET_UP_MODIFIED_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    squares.length should be (64)
    val chessboard = Chessboard.create(squares)
    val pieces = ChessPieceFinder().findChessPieces(chessboard, boardImage)
    pieces should be (32)
  }


  "ChessPieceFinder" should "return 32 when given image " + CHESS_MODIFIED in {
    val img = Imgcodecs.imread(IMG_FOLDER + CHESS_MODIFIED)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (CHESS_MODIFIED_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    squares.length should be (64)
    val chessboard = Chessboard.create(squares)
    val pieces = ChessPieceFinder().findChessPieces(chessboard, boardImage)
    pieces should be (32)
  }


  "ChessPieceFinder" should "return 32 when given image " + CHESS_BOARD_2 in {
    val img = Imgcodecs.imread(IMG_FOLDER + CHESS_BOARD_2)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (CHESS_BOARD_2_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    squares.length should be (64)
    val chessboard = Chessboard.create(squares)
    val pieces = ChessPieceFinder().findChessPieces(chessboard, boardImage)
    pieces should be (32)
  }


  "ChessPieceFinder" should "return 32 when given image " + FENBOARD in {
    val img = Imgcodecs.imread(IMG_FOLDER + FENBOARD)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (FENBOARD_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    squares.length should be (64)
    val chessboard = Chessboard.create(squares)
    val pieces = ChessPieceFinder().findChessPieces(chessboard, boardImage)
    pieces should be (32)
  }


  "ChessPieceFinder" should "return 32 when given image " + FRITZBSMALL in {
    val img = Imgcodecs.imread(IMG_FOLDER + FRITZBSMALL)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (FRITZBSMALL_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    squares.length should be (64)
    val chessboard = Chessboard.create(squares)
    val pieces = ChessPieceFinder().findChessPieces(chessboard, boardImage)
    pieces should be (32)
  }


  "ChessPieceFinder" should "return 32 when given image " + PURPLE in {
    val img = Imgcodecs.imread(IMG_FOLDER + PURPLE)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (PURPLE_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder().getChessboardSquares(boardImage)
    squares.length should be (64)
    val chessboard = Chessboard.create(squares)
    val pieces = ChessPieceFinder().findChessPieces(chessboard, boardImage)
    pieces should be (32)
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
