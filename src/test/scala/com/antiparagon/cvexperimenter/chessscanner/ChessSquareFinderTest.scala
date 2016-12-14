package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.{Core, Mat, Rect}
import org.opencv.imgcodecs.Imgcodecs
import org.scalatest._

/**
  * Created by wmckay on 11/1/16.
  */
class ChessSquareFinderTest extends FlatSpec with Matchers {

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  val BOARD_PIC = "images/Chess Scanner/Game Positions/twic.png"

  "ChessSquareFinder" should "return an empty array when given null to getChessboardSquares()" in {
    val rect = ChessSquareFinder().getChessboardSquares(null)
    rect.length should be (0)
  }

  "ChessSquareFinder" should "return an empty array when given an empty image to getChessboardSquares()" in {
    val rect = ChessSquareFinder().getChessboardSquares(new Mat())
    rect.length should be (0)
  }

  "ChessSquareFinder" should "return an array of 64 ChessSquares when given a chessbaord image" in {
    val img = Imgcodecs.imread(BOARD_PIC)
    val board = new Mat(img, new Rect(217, 140, 322, 319))

    val squares = ChessSquareFinder().getChessboardSquares(board)
    squares.length should be (64)
  }

}
