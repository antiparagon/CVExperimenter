package com.antiparagon.cvexperimenter.chessscanner

import com.antiparagon.cvexperimenter.CVExperimenter
import com.antiparagon.cvexperimenter.tools.ImageTools
import com.typesafe.scalalogging.Logger
import org.opencv.calib3d.Calib3d
import org.opencv.core._
import org.opencv.imgproc.Imgproc
import org.slf4j.LoggerFactory

/**
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
      return None
    }

    val cornerBoard = ChessboardFinderCornerAlgorithm.getChessboard(inImg)
    cornerBoard match {
      case Some(board) => return cornerBoard
      case None => log.info("Corner Algorithm didn't find a chessboard")
    }
    val contoursBoard = ChessboardFinderContoursAlgorithm.getChessboard(inImg)

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

    val cornerRect = ChessboardFinderCornerAlgorithm.findChessboard(inImg)
    cornerRect match {
      case Some(rect) => return cornerRect
      case None => log.info("Corner Algorithm didn't find a chessboard rectangle")
    }
    val contoursRect = ChessboardFinderContoursAlgorithm.findChessboard(inImg)

    return contoursRect
  }
}
