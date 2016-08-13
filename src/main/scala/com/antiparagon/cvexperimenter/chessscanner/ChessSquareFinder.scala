package com.antiparagon.cvexperimenter.chessscanner

import java.util

import com.antiparagon.cvexperimenter.CVExperimenter
import com.antiparagon.cvexperimenter.tools.ImageTools
import org.opencv.calib3d.Calib3d
import org.opencv.core.{CvType, MatOfPoint2f, _}
import org.opencv.imgproc.Imgproc

import scala.collection.immutable.ListMap
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by wmckay on 7/10/16.
  */
object ChessSquareFinder {

  import scala.collection.JavaConversions._

  /**
    * Finds a chessboard in an image and returns a cropped image of
    * just the chessboard.
    *
    * @param inImg with a chessboard
    * @return Option cropped image of only the chessboard
    */
  def getChessboardSquares(inImg: Mat): ArrayBuffer[Rect] = {
    findChessboardSquares(inImg)
  }

  /**
    * Finds a chessboard in an image and returns the rectangle of the found chessboard.
    *
    * @param inImg that contains a chessboard
    * @return Option rectangle coordinates of the chessboard
    */
  def findChessboardSquares(inImg: Mat): ArrayBuffer[Rect] = {

    val tempImg = new Mat
    Imgproc.cvtColor(inImg, tempImg, Imgproc.COLOR_BGR2GRAY)
    Imgproc.GaussianBlur(tempImg, tempImg, new Size(5, 5), 0)
    Imgproc.threshold(tempImg, tempImg, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU)

    val boardSize = new Size(7, 7)
    val squareCorners = new MatOfPoint2f()
    val squares = mutable.ArrayBuffer[Rect]()
    val found = Calib3d.findChessboardCorners(tempImg, boardSize, squareCorners, Calib3d.CALIB_CB_ADAPTIVE_THRESH + Calib3d.CALIB_CB_NORMALIZE_IMAGE + Calib3d.CALIB_CB_FAST_CHECK)
    if(!found) {
      println("Chessboard not found")
      return squares
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

    val numSquares = 42
    avgWidth = avgWidth / numSquares
    avgHeight = avgHeight / numSquares

    // Very top row
    for(col <- 0 until 6) {
      val point = points.get(col)
      val nextPoint = points.get(col + 1)
      if(col == 0) {
        val square = new Rect
        square.x = (point.x - avgWidth).toInt
        if(square.x < 0) square.x = 0
        square.y = (point.y - avgHeight).toInt
        if(square.y < 0 ) square.y = 0
        square.width = (point.x - square.x).toInt
        square.height = (point.y - square.y).toInt
        squares += square
      }
      val square = new Rect
      square.x = point.x.toInt
      square.y = (point.y - avgHeight).toInt
      if(square.y < 0 ) square.y = 0
      square.width = (nextPoint.x.toInt - square.x)
      square.height = (point.y - square.y).toInt
      squares += square
      if(col == 5) {
        val square = new Rect
        square.x = nextPoint.x.toInt
        square.y = (nextPoint.y - avgHeight).toInt
        if(square.y < 0 ) square.y = 0
        square.width = (inImg.width - nextPoint.x).toInt
        square.height = avgHeight.toInt
        squares += square
      }
    }

    // Inner rows
    for(row <- 0 until 6) {
      for(col <- 0 until 6) {
        val index = col + row * 7
        val point = points.get(index)
        val nextPoint = points.get(index + 1)
        val nextRowPoint = points(col + (row + 1) * 7)
        if(col == 0) {
          val square = new Rect
          square.x = (point.x - avgWidth).toInt
          if(square.x < 0) square.x = 0
          square.y = point.y.toInt
          square.width = (nextPoint.x - point.x).toInt
          square.height = (nextRowPoint.y - square.y).toInt
          squares += square
        }
        val square = new Rect
        square.x = point.x.toInt
        square.y = point.y.toInt
        square.width = (nextPoint.x.toInt - square.x)
        square.height = (nextRowPoint.y.toInt - square.y)
        squares += square
        if(col == 5) {
          val square = new Rect
          square.x = nextPoint.x.toInt
          square.y = nextPoint.y.toInt
          square.width = (inImg.width - nextPoint.x).toInt
          square.height = nextRowPoint.y.toInt - square.y
          squares += square
        }
      }
    }

    // Very bottom row
    for(col <- 0 until 6) {
      val index = col + 6 * 7
      val point = points.get(index)
      val nextPoint = points.get(index + 1)
      if(col == 0) {
        val square = new Rect
        square.x = (point.x - avgWidth).toInt
        if(square.x < 0) square.x = 0
        square.y = point.y.toInt
        square.width = (point.x - square.x).toInt
        square.height = inImg.height - square.y
        squares += square
      }
      val square = new Rect
      square.x = point.x.toInt
      square.y = point.y.toInt
      square.width = (nextPoint.x.toInt - square.x)
      square.height = inImg.height - square.y
      squares += square
      if(col == 5) {
        val square = new Rect
        square.x = nextPoint.x.toInt
        square.y = nextPoint.y.toInt
        square.width = (inImg.width - nextPoint.x).toInt
        square.height = inImg.height - square.y
        squares += square
      }
    }

    squares
  }
}
