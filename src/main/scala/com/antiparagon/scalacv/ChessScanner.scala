package com.antiparagon.scalacv

import java.util

import org.opencv.core._
import org.opencv.imgproc.Imgproc

/**
  * Created by wmckay on 4/23/16.
  */
object ChessScanner {

  def findBoard(inImg: Mat): Mat = {

    val tempImg = new Mat
    Imgproc.cvtColor(inImg, tempImg, Imgproc.COLOR_BGR2GRAY)
    Imgproc.GaussianBlur(tempImg, tempImg, new Size(5, 5), 0)
    Imgproc.adaptiveThreshold(tempImg, tempImg, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 11, 2)
    //Imgproc.circle(outImg, new Point(outImg.width()/2, outImg.height()/2), 50, new Scalar(0.0, 255.0, 0.0), 5)


    val contours = new util.ArrayList[MatOfPoint]()
    val hierarchy = new Mat
    Imgproc.findContours(tempImg, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)

    val outImg = new Mat
    Imgproc.cvtColor(tempImg, outImg, Imgproc.COLOR_GRAY2BGR)
    Imgproc.drawContours(outImg, contours, 0, new Scalar(0.0, 255.0, 0.0))

    //Imgproc.circle(outImg, new Point(outImg.width()/2, outImg.height()/2), 50, new Scalar(0.0, 255.0, 0.0), 3)

    return outImg
  }

}
