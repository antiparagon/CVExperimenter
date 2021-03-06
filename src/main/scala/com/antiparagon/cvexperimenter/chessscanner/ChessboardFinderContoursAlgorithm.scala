package com.antiparagon.cvexperimenter.chessscanner

import java.awt.Rectangle
import java.util

import com.typesafe.scalalogging.Logger
import org.opencv.core._
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by wmckay on 11/7/16.
  */
object ChessboardFinderContoursAlgorithm {

  def apply(): ChessboardFinderContoursAlgorithm = {
    new ChessboardFinderContoursAlgorithm
  }

  def apply(debugImagePrefix: String): ChessboardFinderContoursAlgorithm = {
    val chessboardFinder = new ChessboardFinderContoursAlgorithm
    chessboardFinder.outputDebugImgs = true
    chessboardFinder.debugImgPrefix = debugImagePrefix + chessboardFinder.debugImgPrefix
    chessboardFinder
  }
}

/**
  * Uses the OpenCV Imgproc.findContours() function to find rectangles. It looks
  * for a rectangle that contains more than the rectangles inside it that are
  * about 1/64 the area of the inclosing rectangle.
  *
  * Created by wmckay on 10/12/16.
  */
class ChessboardFinderContoursAlgorithm {

  val log = Logger(LoggerFactory.getLogger("ChessboardFinderContoursAlgorithm"))

  /*
    For debugging of the algorithm. Outputs intermediate stage images.
   */
  var outputDebugImgs = false
  // Prefix for debug images
  var debugImgPrefix = "ChessboardFinderContoursAlgorithm"
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

    import scala.collection.JavaConversions._

    if (inImg == null || inImg.empty()) {
      return None
    }

    val tempImg = new Mat
    val debugImg = inImg.clone()
    Imgproc.cvtColor(inImg, tempImg, Imgproc.COLOR_BGR2GRAY)
    //Imgproc.threshold(tempImg, tempImg, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU)
    Imgproc.Canny(tempImg, tempImg, 0, 0)
    Imgproc.threshold(tempImg, tempImg, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU)

    val contours = new util.ArrayList[MatOfPoint]()
    val rectangles = ArrayBuffer[Rect]()
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
          rectangles += rect
          Imgproc.rectangle(debugImg, rect.tl, rect.br, new Scalar(0.0, 255.0, 0.0), 3)
        }
      }
    }

    if(outputDebugImgs) {
      Imgcodecs.imwrite(debugImgPrefix + "_Rectangles.png", debugImg)
    }
    return scanRectList(rectangles)

  }


  /**
    * Scans the list of rectangles and returns a rectangle that contains
    * a rectangle with multiple smaller rectangle inside. It looks for a rectangle
    * that contains at least 3 smaller rectangles with 1/50th the area of the
    * containing rectangle.
    *
    * @param rectList
    * @return Option rectangle coordinates of the rectangle
    */
  def scanRectList(rectList: ArrayBuffer[Rect]): Option[Rect] = {

    if (rectList == null || rectList.size < 4) {
      return None
    }

    //println(s"Number of rectangles: ${rectList.size}")
    val rectMap = mutable.Map[Rectangle, ArrayBuffer[Rectangle]]()
    for (rect <- rectList) {
      // Convert to Java Rectangle
      val rectangle = rect2Rectangle(rect)
      // Check if the lookup contains the Rectangle yet
      if(!rectMap.contains(rectangle)) {
        rectMap.put(rectangle, ArrayBuffer[Rectangle]())
      }
    }

    for (rect <- rectList) {
      // Convert to Java Rectangle
      val rectangle = rect2Rectangle(rect)
      // Calculate the area of the rectangle
      val area = rectangle.width.toDouble * rectangle.height.toDouble
      // Loop through the lookup of Rectangles
      for((jRect, rList) <- rectMap) {
        if(jRect.contains(rectangle)) {
          val ja = jRect.width.toDouble * jRect.height.toDouble
          val multiple = ja / area
          //log.debug(s"Rectangle area $ja has rectangle with $multiple multiple inside")
          if(multiple > 50.0) {
            rectMap(jRect) += rectangle
          }
        }
      }
    }


    var board: Rectangle = null
    var max = 0
    for((jRect, rList) <- rectMap) {
      var squaresInside = rectMap(jRect).size
      //log.debug(s"Rectangle $jRect has $squaresInside rectangles inside")
      if(squaresInside >= 3) {
        if(squaresInside > max) {
          board = jRect
          max = squaresInside
          //log.debug(s"Found a new rectangle with $max rectangles inside: $board")
        }
      }
    }
    if(board == null)
      return None
    else
      return Some(new Rect(board.x, board.y, board.width, board.height))
  }

  def rect2Rectangle(rect: Rect): Rectangle = {
    val rectangle = new Rectangle(rect.x, rect.y, rect.width, rect.height)
    rectangle
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
