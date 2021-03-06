package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.{Core, Mat, Rect}
import org.opencv.imgcodecs.Imgcodecs
import org.scalatest._

/**
  * Created by wmckay on 9/3/16.
  */
class ChessPieceFinderTest extends FlatSpec with Matchers {

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  val BOARD_PIC = "images/Chess Scanner/Starting Position/StartingPosition.png"

  "ChessPieceFinder" should "return 0 when given nulls to findChessPieces()" in {
    val pieces = ChessPieceFinder().findChessPieces(null, null)
    pieces should be (0)
  }

  "ChessPieceFinder" should "return 0 when given an empty chessboard and a empty Mat to findChessPieces()" in {
    val chessboard = new Chessboard()
    val img = new Mat()
    val pieces = ChessPieceFinder().findChessPieces(chessboard, img)
    pieces should be (0)
  }

  "ChessPieceFinder" should "find 0 pieces when given a Mat of a chessbaord and an empty Chessboard object" in {
    val img = Imgcodecs.imread(BOARD_PIC)
    val chessboard = new Chessboard()
    val board = new Mat(img, new Rect(95, 49, 323, 324))
    val pieces = ChessPieceFinder().findChessPieces(chessboard, board)
    pieces should be (0)
  }

  "ChessPieceFinder" should "find 32 pieces when given a Mat of a chessbaord starting position and an initialized Chessboard object" in {

    val img = Imgcodecs.imread(BOARD_PIC)
    val board = new Mat(img, new Rect(95, 49, 323, 324))

    val squares = ChessSquareFinder().getChessboardSquares(board)
    squares.length should be (64)
    val chessboard = Chessboard.create(squares)
    val pieces = ChessPieceFinder().findChessPieces(chessboard, board)
    pieces should be (32)
  }
}
