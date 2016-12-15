package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.{Core, Mat, Rect}
import org.opencv.imgcodecs.Imgcodecs
import org.scalatest._

/**
  * Created by wmckay on 11/3/16.
  */
class ChessSquareFinderCornersAlgorithmTest extends FlatSpec with Matchers {

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  val BOARD_PIC = "images/Chess Scanner/Game Positions/twic.png"

  "ChessSquareFinderCornersAlgorithm" should "return an empty array when given null to getChessboardSquares()" in {
    val rect = ChessSquareFinderCornersAlgorithm().getChessboardSquares(null)
    rect.length should be (0)
  }

  "ChessSquareFinderCornersAlgorithm" should "return an empty array when given an empty image to getChessboardSquares()" in {
    val rect = ChessSquareFinderCornersAlgorithm().getChessboardSquares(new Mat())
    rect.length should be (0)
  }

  "ChessSquareFinderCornersAlgorithm" should "return an array of 64 ChessSquares when given a chessbaord image" in {
    val img = Imgcodecs.imread(BOARD_PIC)
    val board = new Mat(img, new Rect(217, 140, 322, 319))

    val squares = ChessSquareFinderCornersAlgorithm().getChessboardSquares(board)
    squares.length should be (64)
  }

}
