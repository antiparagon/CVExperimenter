package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.{Core, Mat, Rect}
import org.opencv.imgcodecs.Imgcodecs
import org.scalatest._

/**
  * This test file is meant to test all the example chessboard images saved in the
  * 'CVExperimenter/images/Chess Scanner/Starting Position' folder.
  *
  * Created by wmckay on 12/12/16.
  */
class ChessSquareFinderTester extends FlatSpec with Matchers {

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  val IMG_FOLDER = "images/Chess Scanner/Starting Position/"
  val OUTPUT_FOLDER = "Debug Images/"

  val CHESS_BOARD_NEW = "chess-board-new.jpg"
  val CHESS_BOARD_NEW_RECT = new Rect(24, 1, 374, 371)

  val CHESS_KID_MODIFIED = "chesskid_modified.png"
  val CHESS_KID_MODIFIED_RECT = new Rect(42, 10, 632, 632)

  val DIAGRAM_OF_CHESS_BOARD_SETUP_MODIFIED = "diagram-of-chess-board-setup_modified.png"
  val DIAGRAM_OF_CHESS_BOARD_SETUP_MODIFIED_RECT = new Rect(16, 11, 266, 274)

  val FRITZBSMALL = "fritzbsmall.png"
  val FRITZBSMALL_RECT = new Rect(0, 0, 200, 202)

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


  "ChessSquareFinder" should "return Array with 64 Rects when given image " + CHESS_BOARD_NEW in {
    val img = Imgcodecs.imread(IMG_FOLDER + CHESS_BOARD_NEW)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (CHESS_BOARD_NEW_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder(OUTPUT_FOLDER + removeExt(CHESS_BOARD_NEW) + "_").getChessboardSquares(boardImage)
    squares.length should be (64)
  }


  "ChessSquareFinder" should "return Array with 64 Rects when given image " + CHESS_KID_MODIFIED in {
    val img = Imgcodecs.imread(IMG_FOLDER + CHESS_KID_MODIFIED)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (CHESS_KID_MODIFIED_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder(OUTPUT_FOLDER + removeExt(CHESS_KID_MODIFIED) + "_").getChessboardSquares(boardImage)
    squares.length should be (64)
  }


  "ChessSquareFinder" should "return Array with 64 Rects when given image " + DIAGRAM_OF_CHESS_BOARD_SETUP_MODIFIED in {
    val img = Imgcodecs.imread(IMG_FOLDER + DIAGRAM_OF_CHESS_BOARD_SETUP_MODIFIED)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (DIAGRAM_OF_CHESS_BOARD_SETUP_MODIFIED_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder(OUTPUT_FOLDER + removeExt(DIAGRAM_OF_CHESS_BOARD_SETUP_MODIFIED) + "_").getChessboardSquares(boardImage)
    squares.length should be (64)
  }


  "ChessSquareFinder" should "return Array with 64 Rects when given image " + FRITZBSMALL in {
    val img = Imgcodecs.imread(IMG_FOLDER + FRITZBSMALL)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (FRITZBSMALL_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder(OUTPUT_FOLDER + removeExt(FRITZBSMALL) + "_").getChessboardSquares(boardImage)
    squares.length should be (64)
  }


  "ChessSquareFinder" should "return Array with 64 Rects when given image " + KID_CHESS_SETUP_BOARD in {
    val img = Imgcodecs.imread(IMG_FOLDER + KID_CHESS_SETUP_BOARD)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (KID_CHESS_SETUP_BOARD_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder(OUTPUT_FOLDER + removeExt(KID_CHESS_SETUP_BOARD) + "_").getChessboardSquares(boardImage)
    squares.length should be (64)
  }


  "ChessSquareFinder" should "return Array with 64 Rects when given image " + NUMBER in {
    val img = Imgcodecs.imread(IMG_FOLDER + NUMBER)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (NUMBER_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder(OUTPUT_FOLDER + removeExt(NUMBER) + "_").getChessboardSquares(boardImage)
    squares.length should be (64)
  }


  "ChessSquareFinder" should "return Array with 64 Rects when given image " + POSITION in {
    val img = Imgcodecs.imread(IMG_FOLDER + POSITION)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (POSITION_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder(OUTPUT_FOLDER + removeExt(POSITION) + "_").getChessboardSquares(boardImage)
    squares.length should be (64)
  }


  "ChessSquareFinder" should "return Array with 64 Rects when given image " + STAGRAM_MODIFIED in {
    val img = Imgcodecs.imread(IMG_FOLDER + STAGRAM_MODIFIED)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (STAGRAM_MODIFIED_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder(OUTPUT_FOLDER + removeExt(STAGRAM_MODIFIED) + "_").getChessboardSquares(boardImage)
    squares.length should be (64)
  }


  "ChessSquareFinder" should "return Array with 64 Rects when given image " + STARTING_POSITION in {
    val img = Imgcodecs.imread(IMG_FOLDER + STARTING_POSITION)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (STARTING_POSITION_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder(OUTPUT_FOLDER + removeExt(STARTING_POSITION) + "_").getChessboardSquares(boardImage)
    squares.length should be (64)
  }


  "ChessSquareFinder" should "return Array with 64 Rects when given image " + VP_BLACKARRAY_MODIFIED in {
    val img = Imgcodecs.imread(IMG_FOLDER + VP_BLACKARRAY_MODIFIED)
    assert(!img.empty())
    val rect = ChessboardFinder().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (VP_BLACKARRAY_MODIFIED_RECT)
    val boardImage = new Mat(img, rect.get)
    val squares = ChessSquareFinder(OUTPUT_FOLDER + removeExt(VP_BLACKARRAY_MODIFIED) + "_").getChessboardSquares(boardImage)
    squares.length should be (64)
  }


  def removeExt(filename: String): String = {
    if(filename.contains(".")) filename.substring(0, filename.lastIndexOf('.'))
    else filename
  }

}
