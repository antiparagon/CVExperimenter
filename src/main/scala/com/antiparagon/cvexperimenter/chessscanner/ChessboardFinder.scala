package com.antiparagon.cvexperimenter.chessscanner

import com.antiparagon.cvexperimenter.CVExperimenter
import com.antiparagon.cvexperimenter.tools.ImageTools
import com.typesafe.scalalogging.Logger
import org.opencv.calib3d.Calib3d
import org.opencv.core._
import org.opencv.imgproc.Imgproc
import org.slf4j.LoggerFactory

/**
  * This class handles choosing the different algorithms to use to find
  * chessboard in an image. The input image could be a 'raw' image straight from
  * a webcam or a screen grab. This class tries the algorithms to try and find
  * a chessboard in an image.
  *
  * Created by wmckay on 6/12/16.
  */
object ChessboardFinder {

  val log = Logger(LoggerFactory.getLogger("ChessboardFinder"))
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

    log.info("Trying Corner Algorithm in getChessboard()")
    val cornersBoard = ChessboardFinderCornersAlgorithm().getChessboard(inImg)
    cornersBoard match {
      case Some(board) => return cornersBoard
      case None => log.info("Corner Algorithm didn't find a chessboard")
    }
    log.info("Trying Countors Algorithm in getChessboard()")
    val contoursBoard = ChessboardFinderContoursAlgorithm().getChessboard(inImg)

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

    log.info("Trying Corner Algorithm in findChessboard()")
    val cornersRect = ChessboardFinderCornersAlgorithm().findChessboard(inImg)
    cornersRect match {
      case Some(rect) => return cornersRect
      case None => log.info("Corner Algorithm didn't find a chessboard rectangle")
    }
    log.info("Trying Countors Algorithm in findChessboard()")
    val contoursRect = ChessboardFinderContoursAlgorithm().findChessboard(inImg)

    return contoursRect
  }
}
