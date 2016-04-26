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
    //Imgproc.circle(outImg, new Point(outImg.width()/2, outImg.height()/2), 50, new Scalar(0.0, 255.0, 0.0), 5)


    val contours = new util.ArrayList[MatOfPoint]()
    val hierarchy = new Mat
    Imgproc.findContours(tempImg, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)


//    biggest = None
//    max_area = 0
//    for i in contours:
//      area = cv2.contourArea(i)
//      if area > 100:
//        peri = cv2.arcLength(i,True)
//        approx = cv2.approxPolyDP(i,0.02*peri,True)
//        if area > max_area and len(approx)==4:
//          biggest = approx
//          max_area = area

    var biggest = new MatOfPoint2f
    var maxArea = 0.0
    var maxRect = new MatOfPoint
    for(contour <- contours) {
      val area = Imgproc.contourArea(contour)
      println(s"Area: $area")
      if(area > 100.0) {
        contour.convertTo(contour, CvType.CV_32FC2)
        val contour2f = new MatOfPoint2f(contour)
        val peri = Imgproc.arcLength(contour2f, true)
        val approx = new MatOfPoint2f
        Imgproc.approxPolyDP(contour2f, approx, 0.02*peri, true)
        println(s"Size: ${approx.size()}")
        if(area > maxArea && approx.rows == 4) {
          biggest = approx
          maxArea = area
          maxRect = contour
          println("Found rectangle")
        }
      }
    }

    contours.clear()
    contours.add(maxRect)

    println(s"Rects: ${contours.size()}")


    val outImg = new Mat
    Imgproc.cvtColor(tempImg, outImg, Imgproc.COLOR_GRAY2BGR)
    Imgproc.drawContours(outImg, contours, 0, new Scalar(0.0, 255.0, 0.0), 3)

    //Imgproc.circle(outImg, new Point(outImg.width()/2, outImg.height()/2), 50, new Scalar(0.0, 255.0, 0.0), 3)

    return outImg
  }

}
