package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.Mat
import org.scalatest._

/**
  * Created by wmckay on 8/31/16.
  */
class ChessboardFinderTest extends FlatSpec with Matchers {

  System.loadLibrary("opencv_java300")

  "ChessboardFinder" should "return None when given null to getChessboard()" in {
    val rect = ChessboardFinder.getChessboard(null)
    rect should be (None)
  }

  "ChessboardFinder" should "return None when given an empty image to getChessboard()" in {
    val rect = ChessboardFinder.getChessboard(new Mat())
    rect should be (None)
  }

  "ChessboardFinder" should "return None when given null to findChessboard()" in {
    val rect = ChessboardFinder.findChessboard(null)
    rect should be (None)
  }

  "ChessboardFinder" should "return None when given an empty image to findChessboard()" in {
    val rect = ChessboardFinder.findChessboard(new Mat())
    rect should be (None)
  }

}
