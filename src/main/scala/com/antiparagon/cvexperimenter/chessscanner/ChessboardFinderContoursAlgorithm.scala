package com.antiparagon.cvexperimenter.chessscanner

import java.awt.Rectangle
import java.util

import com.antiparagon.cvexperimenter.CVExperimenter
import com.antiparagon.cvexperimenter.tools.ImageTools
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
    new ChessboardFinderContoursAlgorithm()
  }
}

/**
  * Created by wmckay on 10/12/16.
  */
class ChessboardFinderContoursAlgorithm {

  val log = Logger(LoggerFactory.getLogger("ChessboardFinderContoursAlgorithm"))

  /*
    For debugging of the algorithm. Outputs intermediate stage images.
   */
  var outputDebugImgs = false
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


  def scanRectList(rectList: ArrayBuffer[Rect]): Option[Rect] = {

    println(s"Number of rectangles: ${rectList.size}")
    val rectMap = mutable.Map[Rectangle, ArrayBuffer[Rectangle]]()
    for (rect <- rectList) {
      val rectangle = rect2Rectangle(rect)
      if(!rectMap.contains(rectangle)) {
        rectMap.put(rectangle, ArrayBuffer[Rectangle]())
      }
      val area = rectangle.width * rectangle.height
      for((jRect, rList) <- rectMap) {
        val ja = jRect.width * jRect.height
        val multiple = ja / area
        if(multiple < 75 && multiple > 63) {
          rectMap(jRect) += rectangle
        }
      }
    }

    var board: Rectangle = null
    var max = 0;
    for((jRect, rList) <- rectMap) {
      var squaresInside = rectMap(jRect).size
      if(squaresInside > 3) {
        if(squaresInside > max) {
          board = jRect
          max = squaresInside
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
