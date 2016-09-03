package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.{Mat, Rect}
import org.opencv.imgcodecs.Imgcodecs
import org.scalatest._

/**
  * Created by wmckay on 9/3/16.
  */
class ChessPieceFinderTest extends FlatSpec with Matchers {

  System.loadLibrary("opencv_java300")

  "ChessPieceFinder" should "return 0 when given nulls to findChessPieces()" in {
    val pieces = ChessPieceFinder.findChessPieces(null, null)
    pieces should be (0)
  }



}
