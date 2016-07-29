package com.antiparagon.cvexperimenter.chessscanner

import java.util

import com.antiparagon.cvexperimenter.tools.ImageTools
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
    Imgproc.adaptiveThreshold(tempImg, tempImg, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 11, 2)

    val contours = new util.ArrayList[MatOfPoint]()
    val hierarchy = new Mat
    Imgproc.findContours(tempImg, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)

    ImageTools.outputMatProperties(hierarchy)


    val squares = mutable.ArrayBuffer[Rect]()

    val outImg = new Mat
    Imgproc.cvtColor(tempImg, outImg, Imgproc.COLOR_GRAY2BGR)

    val yCoordsRect = mutable.Map[Int, mutable.ArrayBuffer[Rect]]()
    val xCoordsRect = mutable.Map[Int, mutable.ArrayBuffer[Rect]]()

    contours.foreach(contour => {
      val area = Imgproc.contourArea(contour)
      if(area > 100.0) {
        contour.convertTo(contour, CvType.CV_32FC2)
        val contour2f = new MatOfPoint2f(contour)
        val peri = Imgproc.arcLength(contour2f, true)
        val approx = new MatOfPoint2f
        Imgproc.approxPolyDP(contour2f, approx, 0.02*peri, true)
        if(approx.rows == 4) {
          val rect = getBoundingRect(approx)
          squares += rect
          yCoordsRect.getOrElseUpdate(rect.y, mutable.ArrayBuffer[Rect]()) += rect
          xCoordsRect.getOrElseUpdate(rect.x, mutable.ArrayBuffer[Rect]()) += rect
        }
      }
    })


    //println("Rows")
    //outputSquareStats(yCoordsRect)
    //println("Columns")
    //outputSquareStats(xCoordsRect)
    //outputSquares(squares)
    filterSquares(inImg, squares)
  }

  /**
    * Checks to see if a square is about 1/64 the area of the overall chessbaord.
    *
    * @param chessboard
    * @param squares
    * @return array of squares hat are close to the correct area
    */
  def filterSquares(chessboard: Mat, squares: ArrayBuffer[Rect]): ArrayBuffer[Rect] = {
    val filteredSquares = mutable.ArrayBuffer[Rect]()
    val chessboardArea = chessboard.width * chessboard.height
    squares.foreach(square => {
      val area = square.area().toInt
      if(area * 50 > chessboardArea) {
        println(s"FAIL Square area of $area is to big for chessboard area of $chessboardArea")
      } else {
        println(s"PASS Square area of $area is OK for chessboard area of $chessboardArea")
        filteredSquares += square
      }
    })

    return filteredSquares
  }

  def outputSquareStats(coordsRect: mutable.Map[Int, mutable.ArrayBuffer[Rect]]): Unit = {
    var map = ListMap(coordsRect.toSeq.sortBy(_._1):_*)
    var max = 0
    for ((k,v) <- map) {
      println(s"Start coordinate: ${k}, value: ${v}")
      if(v.size > max) max = v.size
    }
    println(s"Max squares: $max")
  }

  /**
    * Draws the squares in the squares array on the provided Mat.
    *
    * @param board
    * @param squares
    */
  def drawSquares(board: Mat, squares: ArrayBuffer[Rect]): Unit = {
    squares.foreach(square => Imgproc.rectangle(board, square.tl, square.br, new Scalar(0.0, 255.0, 0.0), 3))
  }

  /**
    * Draws and 8x8 grid on the provided Mat.
    *
    * @param board
    */
  def drawGrid(board: Mat): Unit = {
    val squareWidth = board.width / 8
    val squareHeight = board.height / 8
    for(row <- 0 to 8) {
      Imgproc.line(board, new Point(0, squareWidth * row), new Point(board.height, squareWidth * row), new Scalar(255.0, 0.0, 0.0), 2)
    }
    for(column <- 0 to 8) {
      Imgproc.line(board, new Point(squareHeight * column, 0), new Point(squareHeight * column, board.width), new Scalar(255.0, 0.0, 0.0), 2)
    }
  }

  /**
    * Outputs the square points.
    *
    * @param squares
    */
  def outputSquares(squares: Seq[Rect]) = {
    squares.foreach(square => println(s"${square.x},${square.y},${square.width},${square.height}"))
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
