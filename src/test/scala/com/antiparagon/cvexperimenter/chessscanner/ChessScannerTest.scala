package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import org.scalatest._

/**
  * Created by wmckay on 6/10/16.
  */
class ChessScannerTest extends FlatSpec with Matchers {

  System.loadLibrary("opencv_java300")

  "ChessboardScanner" should "return None when given null to findChessboard()" in {
    val chessScanner = new ChessScanner
    val img = chessScanner.findChessboard(null)
    img should be (None)
  }

  "ChessboardScanner" should "return a Mat when given image to getChessboard()" in {
    val chessScanner = new ChessScanner
    val img = Imgcodecs.imread("images/twic.png")
    val board = chessScanner.findChessboard(img)
    board.get.width should be (323)
    board.get.height should be (319)
  }

  "ChessScanner" should "return None when given an empty image" in {
    val chessScanner = new ChessScanner
    val pos = chessScanner.getFenPosition()
    pos should be ("8/8/8/8/8/8/8/8")
  }

}
