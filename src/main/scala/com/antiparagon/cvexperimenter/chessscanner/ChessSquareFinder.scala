package com.antiparagon.cvexperimenter.chessscanner

import com.typesafe.scalalogging.Logger
import org.opencv.calib3d.Calib3d
import org.opencv.core._
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.slf4j.LoggerFactory

import scala.collection.mutable


/**
  * Factory methods to create ChessSquareFinder.
  *
  * Created by wmckay on 12/14/16.
  */
object ChessSquareFinder {

  def apply(): ChessSquareFinder = {
    new ChessSquareFinder
  }

  def apply(debugImagePrefix: String): ChessSquareFinder = {
    val chessSquareFinder = new ChessSquareFinder
    chessSquareFinder.outputDebugImgs = true
    chessSquareFinder.debugImgPrefix = debugImagePrefix
    chessSquareFinder.cornersAlgorithm = ChessSquareFinderCornersAlgorithm(chessSquareFinder.debugImgPrefix)
    //chessSquareFinder.contoursAlgorithm = ChessSquareFinderContoursAlgorithm(chessSquareFinder.debugImgPrefix)
    chessSquareFinder
  }
}


/**
  * This class handles choosing the different algorithms to use to find
  * chess squares in a chessboard image. It assumes that the
  * ChessboardFinder code has already been run and that the image
  * is a cropped chessboard image. Currently there is only one
  * algorithm being used (11/11/16).
  *
  * Created by wmckay on 7/10/16.
  */
class ChessSquareFinder {

  val log = Logger(LoggerFactory.getLogger("ChessSquareFinder"))

  /*
    For debugging of the algorithm. Outputs intermediate stage images.
   */
  var outputDebugImgs = false
  // Prefix for debug images
  var debugImgPrefix = "ChessboardFinder_"
  var cornersAlgorithm = ChessSquareFinderCornersAlgorithm()

  /**
    * Finds the Rect of the squares in the image of a chessboard. This function
    * assumes that the inImg is just of a cropped chessboard and nothing else.
    *
    * @param inImg of a chessboard
    * @return Array of Rect of the found squares or empty if unable to find the squares
    */
  def getChessboardSquares(inImg: Mat): Array[Rect] = {

    if(inImg == null) {
      log.debug("Chessboard image null")
      return Array[Rect]()
    }
    if(inImg.empty()) {
      log.debug("Chessboard image empty")
      return Array[Rect]()
    }

    if(outputDebugImgs) {
      Imgcodecs.imwrite(debugImgPrefix + "_InputImg.png", inImg)
    }

    val squares = cornersAlgorithm.getChessboardSquares(inImg)
    return squares
  }
}
