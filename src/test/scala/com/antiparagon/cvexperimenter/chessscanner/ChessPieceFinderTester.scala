package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.{Core, Rect}
import org.opencv.imgcodecs.Imgcodecs
import org.scalatest._

/**
  * This test file is meant to test all the example chessboard images saved in the
  * 'CVExperimenter\images\Chess Scanner\Starting Position\Failures' folder. Those images
  * are the ones that the current algorithms do not work on. If these images are made to work
  * then all saved images will have the algorithms find the chessboards.
  *
  * Created by wmckay on 12/10/16.
  */
class ChessPieceFinderTester extends FlatSpec with Matchers {

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  val IMG_FOLDER = "images/Chess Scanner/Starting Position/Failures/"
  val OUTPUT_FOLDER = "Debug Images/"


  "ChessPieceFinder" should "find 32 pieces when given image " in {

  }



  def removeExt(filename: String): String = {
    if(filename.contains(".")) filename.substring(0, filename.lastIndexOf('.'))
    else filename
  }

}
