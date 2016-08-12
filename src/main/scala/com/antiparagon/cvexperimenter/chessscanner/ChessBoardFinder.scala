package com.antiparagon.cvexperimenter.chessscanner

import java.util

import com.antiparagon.cvexperimenter.CVExperimenter
import com.antiparagon.cvexperimenter.tools.ImageTools
import org.opencv.calib3d.Calib3d
import org.opencv.core.{CvType, MatOfPoint2f, _}
import org.opencv.imgproc.Imgproc

/**
  * Created by wmckay on 6/12/16.
  */
object ChessboardFinder {

  import scala.collection.JavaConversions._

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

    val bbox = findBoard(inImg)
    bbox match {
      case Some(bbox) => {
        val copyImg = inImg.clone()
        Imgproc.rectangle(copyImg, bbox.tl, bbox.br, new Scalar(0.0, 255.0, 0.0), 3)
        CVExperimenter.tabManager.addDebugImageTab("Found chessboard", ImageTools.convertCVtoFX(copyImg))
        Some(new Mat(inImg, bbox))
      }
      case None => {
        println("No chessboard found")
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
  def findBoard(inImg: Mat): Option[Rect] = {

    val tempImg = new Mat
    Imgproc.cvtColor(inImg, tempImg, Imgproc.COLOR_BGR2GRAY)
    Imgproc.GaussianBlur(tempImg, tempImg, new Size(5, 5), 0)
    Imgproc.threshold(tempImg, tempImg, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU)
    //CVExperimenter.tabManager.addDebugImageTab("Threshold image", ImageTools.convertCVtoFX(tempImg))

    val boardSize = new Size(7, 7)
    val squareCorners = new MatOfPoint2f()
    val found = Calib3d.findChessboardCorners(tempImg, boardSize, squareCorners, Calib3d.CALIB_CB_ADAPTIVE_THRESH + Calib3d.CALIB_CB_NORMALIZE_IMAGE + Calib3d.CALIB_CB_FAST_CHECK)
    if(!found) {
      println("Chessboard not found")
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

    val squares = 42
    avgWidth = avgWidth / squares
    avgHeight = avgHeight / squares

    val bbox = new Rect
    bbox.x = (minX - avgWidth).toInt
    bbox.y = (minY - avgHeight).toInt
    bbox.width = (8.0 * avgWidth).toInt
    bbox.height = (8.0 * avgHeight).toInt
    Option(bbox)
  }

}
