package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.{Core, Mat, Rect}
import org.opencv.imgcodecs.Imgcodecs
import org.scalatest._

/**
  * Created by wmckay on 8/31/16.
  */
class ChessboardFinderTest extends FlatSpec with Matchers {

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

  "ChessboardFinder" should "return None when given null to getChessboard()" in {
    val rect = ChessboardFinder.getChessboard(null)
    rect should be (None)
  }

  "ChessboardFinder" should "return None when given an empty image to getChessboard()" in {
    val rect = ChessboardFinder.getChessboard(new Mat())
    rect should be (None)
  }

  "ChessboardFinder" should "return a Mat when given image to getChessboard()" in {
    val img = Imgcodecs.imread("images/twic.png")
    val board = ChessboardFinder.getChessboard(img)
    assert(board.isDefined)
    board.get.width should be (323)
    board.get.height should be (319)
  }

  "ChessboardFinder" should "return None when given null to findChessboard()" in {
    val rect = ChessboardFinder.findChessboard(null)
    rect should be (None)
  }

  "ChessboardFinder" should "return None when given an empty image to findChessboard()" in {
    val rect = ChessboardFinder.findChessboard(new Mat())
    rect should be (None)
  }

  "ChessboardFinder" should "return Rect when given image to findChessboard()" in {
    val img = Imgcodecs.imread("images/twic.png")
    val rect = ChessboardFinder.findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (new Rect(216, 140, 323, 319))
  }

}
