package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.{Core, Mat, Rect}
import org.opencv.imgcodecs.Imgcodecs
import org.scalatest._

import scala.collection.mutable.ArrayBuffer

/**
  * Created by wmckay on 10/29/16.
  */
class ChessboardFinderContoursAlgorithmTest extends FlatSpec with Matchers {

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  val BOARD_PIC = "images/Chess Scanner/Starting Position/purple.png"

  "ChessboardFinderContoursAlgorithm" should "return None when given null to getChessboard()" in {
    val rect = ChessboardFinderContoursAlgorithm().getChessboard(null)
    rect should be (None)
  }

  "ChessboardFinderContoursAlgorithm" should "return None when given an empty image to getChessboard()" in {
    val rect = ChessboardFinderContoursAlgorithm().getChessboard(new Mat())
    rect should be (None)
  }

  "ChessboardFinderContoursAlgorithm" should "return a Mat when given image to getChessboard()" in {
    val img = Imgcodecs.imread(BOARD_PIC)
    val board = ChessboardFinderContoursAlgorithm().getChessboard(img)
    assert(board.isDefined)
    board.get.width should be (251)
    board.get.height should be (252)
  }

  "ChessboardFinderContoursAlgorithm" should "return None when given null to findChessboard()" in {
    val rect = ChessboardFinderContoursAlgorithm().findChessboard(null)
    rect should be (None)
  }

  "ChessboardFinderContoursAlgorithm" should "return None when given an empty image to findChessboard()" in {
    val rect = ChessboardFinderContoursAlgorithm().findChessboard(new Mat())
    rect should be (None)
  }

  "ChessboardFinderContoursAlgorithm" should "return Rect when given image to findChessboard()" in {
    val img = Imgcodecs.imread(BOARD_PIC)
    val rect = ChessboardFinderContoursAlgorithm().findChessboard(img)
    assert(rect.isDefined)
    rect.get should be (new Rect(111, 6, 251, 252))
  }


  "ChessboardFinderContoursAlgorithm.scanRectList()" should "return None given null Rect list" in {
    val rect = ChessboardFinderContoursAlgorithm().scanRectList(null)
    rect should be (None)
  }

  "ChessboardFinderContoursAlgorithm.scanRectList()" should "return None given empty Rect list" in {
    val rect = ChessboardFinderContoursAlgorithm().scanRectList(ArrayBuffer[Rect]())
    rect should be (None)
  }

  "ChessboardFinderContoursAlgorithm.scanRectList()" should "return None given Rect list less then 4 Rect" in {
    val rectList = ArrayBuffer[Rect]()
    rectList += new Rect(0, 0, 100, 100)
    rectList += new Rect(10, 20, 100, 100)
    rectList += new Rect(10, 20, 100, 100)
    val rect = ChessboardFinderContoursAlgorithm().scanRectList(rectList)
    rect should be (None)
  }

}
