package com.antiparagon.cvexperimenter.chessscanner

import com.antiparagon.cvexperimenter.CVExperimenter
import com.typesafe.scalalogging.Logger
import org.opencv.calib3d.Calib3d
import org.opencv.core._
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.slf4j.LoggerFactory

/**
  * Created by wmckay on 11/7/16.
  */
object ChessboardFinderCornersAlgorithm {

  def apply(): ChessboardFinderCornersAlgorithm = {
    new ChessboardFinderCornersAlgorithm
  }
}

/**
  * Uses the OpenCV library function Calib3d.findChessboardCorners() that is
  * suppoesed to be used for calibrate cameras. The function is perfect for
  * finding the chessboard in an image and for getting the coordinates of the
  * individual squares.
  *
  * Created by wmckay on 6/12/16.
  */
class ChessboardFinderCornersAlgorithm {

  val log = Logger(LoggerFactory.getLogger("ChessboardFinderCornerAlgorithm"))

  /*
    For debugging of the algorithm. Outputs intermediate stage images.
   */
  var outputDebugImgs = false
  // Prefix for debug images
  var debugImgPrefix = "ChessboardFinderCornersAlgorithm"
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

    val bbox = findChessboard(inImg)
    bbox match {
      case Some(bbox) => {
        Some(new Mat(inImg, bbox))
      }
      case None => {
        log.debug("No chessboard found")
        None
      }
    }
  }

  /**
    * Finds a chessboard in an image and returns the rectangle of the found chessboard.
    *
    * @param inImg that contains a chessboard
    * @return Option rectangle coordinates of the chessboard
    */
  def findChessboard(inImg: Mat): Option[Rect] = {

    if(inImg == null || inImg.empty()) {
      return None
    }
    val tempImg = new Mat
    Imgproc.cvtColor(inImg, tempImg, Imgproc.COLOR_BGR2GRAY)
    //Imgproc.GaussianBlur(tempImg, tempImg, new Size(5, 5), 0)
    if(outputDebugImgs) {
      //CVExperimenter.tabManager.addDebugImageTab("Blurred image", ImageTools.convertCVtoFX(tempImg))
      Imgcodecs.imwrite(debugImgPrefix + "_BlurredImg.png", tempImg)
    }

    Imgproc.threshold(tempImg, tempImg, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU)
    //Imgproc.adaptiveThreshold(tempImg, tempImg, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 15, 40)
    if(outputDebugImgs) {
      //CVExperimenter.tabManager.addDebugImageTab("Adaptive Threshold image", ImageTools.convertCVtoFX(tempImg))
      Imgcodecs.imwrite(debugImgPrefix + "_AdaptiveThreshold.png", tempImg)
    }

    val boardSize = new Size(7, 7)
    val squareCorners = new MatOfPoint2f()
    val found = Calib3d.findChessboardCorners(tempImg, boardSize, squareCorners, Calib3d.CALIB_CB_ADAPTIVE_THRESH + Calib3d.CALIB_CB_NORMALIZE_IMAGE +
                                                                                 Calib3d.CALIB_CB_FILTER_QUADS + Calib3d.CALIB_CB_FAST_CHECK)
    if(!found) {
      log.debug("Chessboard not found")
      if(!CVExperimenter.USE_CHESSSCANNER_ON_VIDEOTAB) { // Create debug tab if not using the webcam
        //CVExperimenter.tabManager.addDebugImageTab("Threshold image", ImageTools.convertCVtoFX(tempImg))
        Calib3d.drawChessboardCorners(tempImg, boardSize, squareCorners, false)
        if(outputDebugImgs) {
          //CVExperimenter.tabManager.addDebugImageTab("Found squares", ImageTools.convertCVtoFX(tempImg))
          Imgcodecs.imwrite(debugImgPrefix + "_FoundSquares.png", tempImg)
        }
      }
      return None
    }
    val term = new TermCriteria(TermCriteria.EPS | TermCriteria.MAX_ITER, 30, 0.1)
    Imgproc.cornerSubPix(tempImg, squareCorners, new Size(11, 11), new Size(-1, -1), term)

    var minX = Double.MaxValue
    var maxX = Double.MinValue
    var minY = Double.MaxValue
    var maxY = Double.MinValue
    var avgWidth = 0.0
    var avgHeight = 0.0
    val points = squareCorners.toList
    for(row <- 0 until 7) {
      for(col <- 0 until 7) {
        val index = col + row * 7
        val point = points.get(index)
        if(point.x < minX) minX = point.x
        if(point.x > maxX) maxX = point.x
        if(point.y < minY) minY = point.y
        if(point.y > maxY) maxY = point.y
        if(col > 0) {
          avgWidth += (point.x - points.get(index - 1).x).abs
        }
        if(row > 0) {
          val tempIndex = col + (row - 1) * 7
          avgHeight += (point.y - points.get(tempIndex).y).abs
        }
      }
    }

    val squares = 42 // Number of interations in double for loop above
    avgWidth = avgWidth / squares
    avgHeight = avgHeight / squares

    val bbox = new Rect
    bbox.x = (minX - avgWidth).toInt
    if(bbox.x < 0) bbox.x = 0
    bbox.y = (minY - avgHeight).toInt
    if(bbox.y < 0) bbox.y = 0

    bbox.width = (maxX - minX + 2.0 * avgWidth).toInt
    if((bbox.x + bbox.width) > inImg.width) {
      bbox.width = inImg.width - bbox.x
    }
    bbox.height = (maxY - minY + 2.0 * avgHeight).toInt
    if((bbox.y + bbox.height) > inImg.height) {
      bbox.height = inImg.height - bbox.y
    }
    Option(bbox)
  }

}
