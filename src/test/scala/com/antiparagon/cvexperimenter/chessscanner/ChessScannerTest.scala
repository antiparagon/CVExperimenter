package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.{Core, Mat}
import org.opencv.imgcodecs.Imgcodecs
import org.scalatest._

/**
  * Created by wmckay on 6/10/16.
  */
class ChessScannerTest extends FlatSpec with Matchers {

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  val TWIC_PIC = "images/Chess Scanner/Game Positions/twic.png"

  "ChessboardScanner" should "return None when given null to findChessboard()" in {
    val chessScanner = new ChessScanner
    val img = chessScanner.findChessboard(null)
    img should be (None)
  }

  "ChessboardScanner" should "return a Mat when given image to getChessboard()" in {
    val chessScanner = new ChessScanner
    val img = Imgcodecs.imread(TWIC_PIC)
    val board = chessScanner.findChessboard(img)
    assert(board.isDefined)
    board.get.width should be (322)
    board.get.height should be (319)
  }

  "ChessScanner" should "return None when given an empty image" in {
    val chessScanner = new ChessScanner
    val pos = chessScanner.getFenPosition()
    pos should be ("8/8/8/8/8/8/8/8")
  }

}
