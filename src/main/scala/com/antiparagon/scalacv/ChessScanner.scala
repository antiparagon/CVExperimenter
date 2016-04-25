package com.antiparagon.scalacv

import org.opencv.core.{Mat, Point, Scalar}
import org.opencv.imgproc.Imgproc

/**
  * Created by wmckay on 4/23/16.
  */
object ChessScanner {

  def findBoard(inImg: Mat): Mat = {

    val outImg = new Mat
    Imgproc.cvtColor(inImg, outImg, Imgproc.COLOR_BGR2GRAY)

    Imgproc.circle(outImg, new Point(outImg.width()/2, outImg.height()/2), 50, new Scalar(0.0, 255.0, 0.0), 5)


    return outImg
  }

}
