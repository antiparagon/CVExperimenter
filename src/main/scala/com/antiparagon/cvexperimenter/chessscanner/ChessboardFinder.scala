package com.antiparagon.cvexperimenter.chessscanner

import com.typesafe.scalalogging.Logger
import org.opencv.core._
import org.slf4j.LoggerFactory


/**
  * Factory methods to create ChessboardFinder.
  *
  * Created by wmckay on 11/15/16.
  */
object ChessboardFinder {

  def apply(): ChessboardFinder = {
    new ChessboardFinder
  }

  def apply(debugImagePrefix: String): ChessboardFinder = {
    val chessboardFinder = new ChessboardFinder
    chessboardFinder.outputDebugImgs = true
    chessboardFinder.debugImgPrefix = debugImagePrefix
    chessboardFinder.cornersAlgorithm = ChessboardFinderCornersAlgorithm(chessboardFinder.debugImgPrefix)
    chessboardFinder.contoursAlgorithm = ChessboardFinderContoursAlgorithm(chessboardFinder.debugImgPrefix)
    chessboardFinder
  }
}

/**
  * This class handles choosing the different algorithms to use to find
  * chessboard in an image. The input image could be a 'raw' image straight from
  * a webcam or a screen grab. This class tries the algorithms to try and find
  * a chessboard in an image.
  *
  * Created by wmckay on 6/12/16.
  */
class ChessboardFinder {

  val log = Logger(LoggerFactory.getLogger("ChessboardFinder"))

  /*
    For debugging of the algorithm. Outputs intermediate stage images.
   */
  var outputDebugImgs = false
  // Prefix for debug images
  var debugImgPrefix = "ChessboardFinder_"
  var cornersAlgorithm = ChessboardFinderCornersAlgorithm()
  var contoursAlgorithm = ChessboardFinderContoursAlgorithm()

  /**
    * Finds a chessboard in an image and returns a cropped image of
    * just the chessboard.
    *
    * @param inImg with a chessboard
    * @return Option cropped image of only the chessboard
    */
  def getChessboard(inImg: Mat): Option[Mat] = {
    if(inImg == null) {
      log.debug("Input image null")
      return None
    }
    if(inImg.empty()) {
      log.debug("Input image empty")
      return None
    }

    //log.info("Trying Corner Algorithm in getChessboard()")
    val cornersBoard = cornersAlgorithm.getChessboard(inImg)
    cornersBoard match {
      case Some(board) => return cornersBoard
      case None => log.info("Corner Algorithm didn't find a chessboard")
    }
    //log.info("Trying Countors Algorithm in getChessboard()")
    val contoursBoard = contoursAlgorithm.getChessboard(inImg)

    return contoursBoard

  }

  /**
    * Finds a chessboard in an image and returns the rectangle of the found chessboard.
    *
    * @param inImg that contains a chessboard
    * @return Option rectangle coordinates of the chessboard
    */
  def findChessboard(inImg: Mat): Option[Rect] = {

    if (inImg == null || inImg.empty()) {
      return None
    }

    //log.info("Trying Corner Algorithm in findChessboard()")
    val cornersRect = cornersAlgorithm.findChessboard(inImg)
    cornersRect match {
      case Some(rect) => return cornersRect
      case None => //log.info("Corner Algorithm didn't find a chessboard rectangle")
    }
    //log.info("Trying Countors Algorithm in findChessboard()")
    val contoursRect = contoursAlgorithm.findChessboard(inImg)

    return contoursRect
  }
}
