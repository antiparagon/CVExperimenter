package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.{Core, Mat, Rect}
import org.opencv.imgcodecs.Imgcodecs
import org.scalatest._

/**
  * Created by wmckay on 10/29/16.
  */
class ChessboardFinderCornerAlgorithmTest extends FlatSpec with Matchers {

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  val TWIC_PIC = "images/Chess Scanner/Game Positions/twic.png"

  "ChessboardFinderCornerAlgorithm" should "return None when given null to getChessboard()" in {
    val rect = ChessboardFinderCornerAlgorithm.getChessboard(null)
    rect should be (None)
  }

  "ChessboardFinderCornerAlgorithm" should "return None when given an empty image to getChessboard()" in {
    val rect = ChessboardFinderCornerAlgorithm.getChessboard(new Mat())
    rect should be (None)
  }

  "ChessboardFinderCornerAlgorithm" should "return a Mat when given image to getChessboard()" in {
    val img = Imgcodecs.imread(TWIC_PIC)
    val board = ChessboardFinderCornerAlgorithm.getChessboard(img)
    assert(board.isDefined)
    board.get.width should be (322)
    board.get.height should be (319)
  }

  "ChessboardFinderCornerAlgorithm" should "return None when given null to findChessboard()" in {
    val rect = ChessboardFinderCornerAlgorithm.findChessboard(null)
    rect should be (None)
  }

  "ChessboardFinderCornerAlgorithm" should "return None when given an empty image to findChessboard()" in {
    val rect = ChessboardFinderCornerAlgorithm.findChessboard(new Mat())
    rect should be (None)
  }

  "ChessboardFinderCornerAlgorithm" should "return Rect when given image to findChessboard()" in {
    val img = Imgcodecs.imread(TWIC_PIC)
    val rect = ChessboardFinderCornerAlgorithm.findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (new Rect(217, 140, 322, 319))
  }

}
