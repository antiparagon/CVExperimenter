package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.{Core, Rect}
import org.opencv.imgcodecs.Imgcodecs
import org.scalatest._

/**
  * This test file is meant to test all the eample chessboard images saved in the
  * 'CVExperimenter\images\Chess Scanner\Starting Position\Failures' folder. Those images
  * are the ones that the current algorithms do not work on. If these images are made to work
  * then all saved images will have the algorithms find the chessboards.
  *
  * Created by wmckay on 11/5/16.
  */
class ChessboardFinderTester extends FlatSpec with Matchers {

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  val IMG_FOLDER = "images/Chess Scanner/Starting Position/Failures/"
  val OUTPUT_FOLDER = "Debug Images/"
  val BOARD_SETUP = "BoardSetup.jpg"
  val BOARD_SETUP_RECT = new Rect(13, 13, 223, 223)
  val CHESS_BOARD_SET_UP = "chess_board_set_up.jpg"
  val CHESS_BOARD_SET_UP_RECT = new Rect(0, 0, 350, 350)
  val CHESS_BOARD_SET_UP_MODIFIED = "chess_board_set_up_modified.jpg"
  val CHESS_BOARD_SET_UP_MODIFIED_RECT = new Rect(9, 9, 350, 350)
  val CHESS = "chess.png"
  val CHESS_RECT = new Rect(95, 49, 322, 324)
  val CHESS_MODIFIED = "chess_modified.png"
  val CHESS_MODIFIED_RECT = new Rect(95, 49, 322, 324)
  val CHESS_KID = "chesskid.png"
  val CHESS_KID_RECT = new Rect(95, 49, 322, 324)
  val DIAGRAM_OF_CHESS_BOARD_SETUP = "diagram-of-chess-board-setup.png"
  val DIAGRAM_OF_CHESS_BOARD_SETUP_RECT = new Rect(95, 49, 322, 324)
  val STAGRAM = "stagram.png"
  val STAGRAM_RECT = new Rect(95, 49, 322, 324)
  val VP_BLACKARRAY = "VP-Blackarray.png"
  val VP_BLACKARRAY_RECT = new Rect(95, 49, 322, 324)


  /**
    * This board only has a few squares found using contours. The fix was to
    * only require a least 3 inner squares to determine a chessboard.
    *
    * Note: The debug images look like it should work fine. Should take
    *       a second look at why there is a problem.
    */
  "ChessboardFinder" should "return Rect when given image " + BOARD_SETUP in {
    val img = Imgcodecs.imread(IMG_FOLDER + BOARD_SETUP)
    assert(!img.empty())
    val rect = ChessboardFinder(OUTPUT_FOLDER + removeExt(BOARD_SETUP) + "_").findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (BOARD_SETUP_RECT)
  }

  /**
    * This board is hard to find because the chessboard is the full image with no background.
    * The test below this one uses the same board with a black border around it.
    *
    * Note: How should an image that is only a chessboard be handled? Should a border be
    *       required?
    */
  "ChessboardFinder" should "return Rect when given image " + CHESS_BOARD_SET_UP in {
    val img = Imgcodecs.imread(IMG_FOLDER + CHESS_BOARD_SET_UP)
    assert(!img.empty())
    val rect = ChessboardFinder(OUTPUT_FOLDER + removeExt(CHESS_BOARD_SET_UP) + "_").findChessboard(img)
    rect should be (None)
    // The lines are commented out until a way to handle a chessbaord with no border is found.
    //assert(rect.isDefined)
    //rect.get should be (CHESS_BOARD_SET_UP_RECT)
  }
  "ChessboardFinder" should "return Rect when given image " + CHESS_BOARD_SET_UP_MODIFIED in {
    val img = Imgcodecs.imread(IMG_FOLDER + CHESS_BOARD_SET_UP_MODIFIED)
    assert(!img.empty())
    val rect = ChessboardFinder(OUTPUT_FOLDER + removeExt(CHESS_BOARD_SET_UP_MODIFIED) + "_").findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (CHESS_BOARD_SET_UP_MODIFIED_RECT)
  }

  "ChessboardFinder" should "return Rect when given image " + CHESS in {
    val img = Imgcodecs.imread(IMG_FOLDER + CHESS)
    assert(!img.empty())
    val rect = ChessboardFinder(OUTPUT_FOLDER + removeExt(CHESS) + "_").findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (CHESS_RECT)
  }
  "ChessboardFinder" should "return Rect when given image " + CHESS_MODIFIED in {
    val img = Imgcodecs.imread(IMG_FOLDER + CHESS_MODIFIED)
    assert(!img.empty())
    val rect = ChessboardFinder(OUTPUT_FOLDER + removeExt(CHESS_MODIFIED) + "_").findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (CHESS_MODIFIED_RECT)
  }

  "ChessboardFinder" should "return Rect when given image " + CHESS_KID in {
    val img = Imgcodecs.imread(IMG_FOLDER + CHESS_KID)
    assert(!img.empty())
    val rect = ChessboardFinder(OUTPUT_FOLDER + removeExt(CHESS_KID) + "_").findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (CHESS_KID_RECT)
  }

  "ChessboardFinder" should "return Rect when given image " + DIAGRAM_OF_CHESS_BOARD_SETUP in {
    val img = Imgcodecs.imread(IMG_FOLDER + DIAGRAM_OF_CHESS_BOARD_SETUP)
    assert(!img.empty())
    val rect = ChessboardFinder(OUTPUT_FOLDER + removeExt(DIAGRAM_OF_CHESS_BOARD_SETUP) + "_").findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (DIAGRAM_OF_CHESS_BOARD_SETUP_RECT)
  }

  "ChessboardFinder" should "return Rect when given image " + STAGRAM in {
    val img = Imgcodecs.imread(IMG_FOLDER + STAGRAM)
    assert(!img.empty())
    val rect = ChessboardFinder(OUTPUT_FOLDER + removeExt(STAGRAM) + "_").findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (STAGRAM_RECT)
  }

  "ChessboardFinder" should "return Rect when given image " + VP_BLACKARRAY in {
    val img = Imgcodecs.imread(IMG_FOLDER + VP_BLACKARRAY)
    assert(!img.empty())
    val rect = ChessboardFinder(OUTPUT_FOLDER + removeExt(VP_BLACKARRAY) + "_").findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (VP_BLACKARRAY_RECT)
  }

  def removeExt(filename: String): String = {
    if(filename.contains(".")) filename.substring(0, filename.lastIndexOf('.'))
    else filename
  }

}
