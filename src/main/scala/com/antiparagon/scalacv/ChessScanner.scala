package com.antiparagon.scalacv

import java.util

import org.opencv.core._
import org.opencv.imgproc.Imgproc


/**
  * Created by wmckay on 4/23/16.
  */
object ChessScanner {

  import scala.collection.JavaConversions._
  import scala.collection.JavaConversions.asScalaIterator

  def findBoard(inImg: Mat): Mat = {

    val tempImg = new Mat
    Imgproc.cvtColor(inImg, tempImg, Imgproc.COLOR_BGR2GRAY)
    Imgproc.GaussianBlur(tempImg, tempImg, new Size(5, 5), 0)
    Imgproc.adaptiveThreshold(tempImg, tempImg, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 11, 2)
    
    val contours = new util.ArrayList[MatOfPoint]()
    val hierarchy = new Mat
    Imgproc.findContours(tempImg, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)


    var biggest = new MatOfPoint2f
    var maxArea = 0.0

    val outImg = new Mat
    Imgproc.cvtColor(tempImg, outImg, Imgproc.COLOR_GRAY2BGR)

    var index = 0
    for(contour <- contours) {
      val area = Imgproc.contourArea(contour)
      if(area > 100.0) {
        contour.convertTo(contour, CvType.CV_32FC2)
        val contour2f = new MatOfPoint2f(contour)
        val peri = Imgproc.arcLength(contour2f, true)
        val approx = new MatOfPoint2f
        Imgproc.approxPolyDP(contour2f, approx, 0.02*peri, true)
        if(area > maxArea && approx.rows == 4) {
          biggest = approx
          maxArea = area
        }
      }
      index += 1
    }

    contours.clear()
    biggest.convertTo(biggest, CvType.CV_32S)
    val maxRect = new MatOfPoint
    biggest.convertTo(maxRect, CvType.CV_32S)
    contours.add(maxRect)

    Imgproc.polylines(inImg, contours, true, new Scalar(0.0, 255.0, 0.0))

    return inImg
  }


  def printMat(mat: Mat) = {
    for(i <- 0 to mat.rows) {
      for (j <- 0 to mat.cols) {
        val elem = mat.get(i, j)
        if(elem != null)
          for(e <- elem)
            print(s"$e ")
      }
      println()
    }
  }

}
