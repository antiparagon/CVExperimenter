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

    findBoard(inImg)

    /*
    val bbox = findBoard(inImg)
    bbox match {
      case Some(bbox) => {
        //Imgproc.rectangle(inImg, bbox.tl, bbox.br, new Scalar(0.0, 255.0, 0.0), 3)
        //return Option(inImg)
        return Some(new Mat(inImg, bbox))
      }
      case None => {
        println("No chessboard found")
        return None
      }
    }
    */
  }

  /**
    * Finds a chessboard in an image and returns the rectangle of the found chessboard.
    *
    * @param inImg that contains a chessboard
    * @return Option rectangle coordinates of the chessboard
    */
  def findBoard(inImg: Mat): Option[Mat] = {

    val tempImg = new Mat
    Imgproc.cvtColor(inImg, tempImg, Imgproc.COLOR_BGR2GRAY)
    Imgproc.GaussianBlur(tempImg, tempImg, new Size(5, 5), 0)
    Imgproc.threshold(tempImg, tempImg, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU)
    CVExperimenter.tabManager.addDebugImageTab("Threshold image", ImageTools.convertCVtoFX(tempImg))

    val boardSize = new Size(7, 7)
    val squareCorners = new MatOfPoint2f()
    val found = Calib3d.findChessboardCorners(tempImg, boardSize, squareCorners, Calib3d.CALIB_CB_ADAPTIVE_THRESH + Calib3d.CALIB_CB_NORMALIZE_IMAGE + Calib3d.CALIB_CB_FAST_CHECK)
    if(!found) {
      println("Chessboard not found")
      return None
    }
    val term = new TermCriteria(TermCriteria.EPS | TermCriteria.MAX_ITER, 30, 0.1)
    Imgproc.cornerSubPix(tempImg, squareCorners, new Size(11, 11), new Size(-1, -1), term)
    ImageTools.printMat(squareCorners)

    var minX = Double.MaxValue
    var maxX = Double.MinValue
    var minY = Double.MaxValue
    var maxY = Double.MinValue
    var avgWidth = 0.0
    var avgHeight = 0.0

    val points = squareCorners.toList
    var loops = 0.0
    for(i <- 0 to points.size - 1) {
      val point = points.get(i)
      if(point.x < minX) minX = point.x
      if(point.x > maxX) maxX = point.x
      if(point.y < minY) minY = point.y
      if(point.y > maxX) maxY = point.y
      avgWidth += (points.get(i + 1).x - point.x).abs
      avgHeight += (point.y - points.get(i + 1).y).abs
      loops += 1.0
    }
    if(points.get(points.size - 1).x < minX) minX = points.get(points.size - 1).x
    if(points.get(points.size - 1).x > maxX) maxX = points.get(points.size - 1).x
    if(points.get(points.size - 1).y < minY) minY = points.get(points.size - 1).y
    if(points.get(points.size - 1).y > maxX) maxY = points.get(points.size - 1).y
    avgWidth = avgWidth / loops
    avgHeight = avgHeight / loops

    println(s"Min x: $minX")
    println(s"Max x: $maxX")
    println(s"Min y: $minY")
    println(s"Max y: $maxY")
    println(s"Avg width: $avgWidth")
    println(s"Avg height: $avgHeight")

    Calib3d.drawChessboardCorners(inImg, boardSize, squareCorners, found)
    Some(inImg)
  }

  /**
    * Finds a chessboard in an image and returns the rectangle of the found chessboard.
    *
    * @param inImg that contains a chessboard
    * @return Option rectangle coordinates of the chessboard
    */
  def findBoardOld(inImg: Mat): Option[Rect] = {

    val tempImg = new Mat
    Imgproc.cvtColor(inImg, tempImg, Imgproc.COLOR_BGR2GRAY)
    Imgproc.GaussianBlur(tempImg, tempImg, new Size(5, 5), 0)
    Imgproc.adaptiveThreshold(tempImg, tempImg, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 11, 2)

    val contours = new util.ArrayList[MatOfPoint]()
    val hierarchy = new Mat
    Imgproc.findContours(tempImg, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)

    //ImageTools.outputMatProperties(hierarchy)
    // What is findContours doing?

    var biggest = new MatOfPoint2f
    var maxArea = 0.0

    val outImg = new Mat
    Imgproc.cvtColor(tempImg, outImg, Imgproc.COLOR_GRAY2BGR)

    contours.foreach(contour => {
      val area = Imgproc.contourArea(contour)
      if(area > 100.0) {
        contour.convertTo(contour, CvType.CV_32FC2)
        val contour2f = new MatOfPoint2f(contour)
        val peri = Imgproc.arcLength(contour2f, true)
        // What is peri?
        val approx = new MatOfPoint2f
        Imgproc.approxPolyDP(contour2f, approx, 0.02*peri, true)
        // What is approxPolyDP?
        if(area > maxArea && approx.rows == 4) {
          biggest = approx
          maxArea = area
        }
      }
    })

    if(maxArea > 0.0) {
      val maxRect = new MatOfPoint
      biggest.convertTo(maxRect, CvType.CV_32S)
      val bbox = getBoundingRect(maxRect)
      return Option(bbox)
    }
    return None
  }

  /**
    * Returns the bounding Rect from the MatOfPoints.
    *
    * @param points to bound
    * @return bounding Rect
    */
  def getBoundingRect(points: MatOfPoint): Rect = {
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
