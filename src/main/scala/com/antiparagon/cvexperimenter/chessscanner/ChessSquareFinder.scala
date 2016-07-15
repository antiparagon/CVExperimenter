package com.antiparagon.cvexperimenter.chessscanner

import java.util

import org.opencv.core.{CvType, MatOfPoint2f, _}
import org.opencv.imgproc.Imgproc

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
    Imgproc.adaptiveThreshold(tempImg, tempImg, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 11, 2)

    val contours = new util.ArrayList[MatOfPoint]()
    val hierarchy = new Mat
    Imgproc.findContours(tempImg, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)

    val squares = mutable.ArrayBuffer[Rect]()

    val outImg = new Mat
    Imgproc.cvtColor(tempImg, outImg, Imgproc.COLOR_GRAY2BGR)

    for(contour <- contours) {
      val area = Imgproc.contourArea(contour)
      if(area > 100.0) {
        contour.convertTo(contour, CvType.CV_32FC2)
        val contour2f = new MatOfPoint2f(contour)
        val peri = Imgproc.arcLength(contour2f, true)
        val approx = new MatOfPoint2f
        Imgproc.approxPolyDP(contour2f, approx, 0.02*peri, true)
        if(approx.rows == 4) {
          squares += getBoundingRect(approx)
        }
      }
    }
    outputSquares(squares)
    return squares
  }

  def outputSquares(squares: Seq[Rect]): Unit = {

    for(square <- squares) {
      println(s"${square.x},${square.y},${square.width},${square.height}")
    }

  }

  /**
    * Returns the bounding Rect from the MatOfPoints.
    *
    * @param points to bound
    * @return bounding Rect
    */
  def getBoundingRect(points: MatOfPoint2f): Rect = {
    val bbox = new Rect
    var minX = Double.MaxValue
    var maxX = Double.MinValue
    var minY = Double.MaxValue
    var maxY = Double.MinValue

    for(point <- points.toArray) {
      if(point.x > maxX) maxX = point.x
      if(point.x < minX) minX = point.x
      if(point.y > maxY) maxY = point.y
      if(point.y < minY) minY = point.y
    }

    bbox.x = minX.toInt
    bbox.y = minY.toInt
    bbox.width = (maxX - minX).toInt
    bbox.height = (maxY - minY).toInt
    bbox
  }
}
