package com.antiparagon.cvexperimenter.chessscanner

import java.util

import com.antiparagon.cvexperimenter.CVExperimenter
import com.antiparagon.cvexperimenter.tools.ImageTools
import com.typesafe.scalalogging.Logger
import org.opencv.core._
import org.opencv.imgproc.Imgproc
import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by wmckay on 10/12/16.
  */
object ChessboardTestFinder {

  val log = Logger(LoggerFactory.getLogger("ChessboardTestFinder"))
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

    import scala.collection.JavaConversions._

    if (inImg == null || inImg.empty()) {
      return None
    }

    val tempImg = new Mat
    val debugImg = inImg.clone()
    Imgproc.cvtColor(inImg, tempImg, Imgproc.COLOR_BGR2GRAY)
    Imgproc.threshold(tempImg, tempImg, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU)
    CVExperimenter.tabManager.addDebugImageTab("Adaptive Threshold image", ImageTools.convertCVtoFX(tempImg))

    var biggest = new MatOfPoint2f
    var maxArea = 0.0

    val contours = new util.ArrayList[MatOfPoint]()
    val hierarchy = new Mat
    Imgproc.findContours(tempImg, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)

    for (contour <- contours) {
      val area = Imgproc.contourArea(contour)
      //println(s"Area: $area")
      if (area > 100.0) {
        //contourSave = contour.clone().asInstanceOf[MatOfPoint]
        contour.convertTo(contour, CvType.CV_32FC2)
        val contour2f = new MatOfPoint2f(contour)
        val peri = Imgproc.arcLength(contour2f, true)
        val approx = new MatOfPoint2f
        Imgproc.approxPolyDP(contour2f, approx, 0.02 * peri, true)
        //println(s"Size: ${approx.size()}")
        if(approx.rows == 4) {
          val rectPoints = new MatOfPoint
          approx.convertTo(rectPoints, CvType.CV_32S)
          val rect = getBoundingRect(rectPoints)
          Imgproc.rectangle(debugImg, rect.tl, rect.br, new Scalar(0.0, 255.0, 0.0), 3)
          if (area > maxArea) {
            biggest = approx
            maxArea = area
            println(s"Found rectangle: $contour")
            Imgproc.rectangle(debugImg, rect.tl, rect.br, new Scalar(0.0, 0.0, 255.0), 3)
            //return outImg
          }
        }

      }
    }
    CVExperimenter.tabManager.addDebugImageTab("Rectangles found", ImageTools.convertCVtoFX(debugImg))
    contours.clear()
    //biggest.convertTo(biggest, CvType.CV_32S)
    val maxRect = new MatOfPoint
    biggest.convertTo(maxRect, CvType.CV_32S)
    contours.add(maxRect)
    Some(getBoundingRect(maxRect))
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
    var map = mutable.ListMap(coordsRect.toSeq.sortBy(_._1):_*)
    var max = 0
    for ((k,v) <- map) {
      println(s"Start coordinate: ${k}, value: ${v}")
      if(v.size > max) max = v.size
    }
    println(s"Max squares: $max")
  }

  def getBoundingRect(rect: MatOfPoint): Rect = {
    val bbox = new Rect
    var minX = Double.MaxValue
    var maxX = Double.MinValue
    var minY = Double.MaxValue
    var maxY = Double.MinValue

    for(point <- rect.toArray) {
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
