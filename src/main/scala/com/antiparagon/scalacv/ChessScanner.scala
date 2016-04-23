package com.antiparagon.scalacv

import org.opencv.core.{Mat, Point, Scalar}
import org.opencv.imgproc.Imgproc

/**
  * Created by wmckay on 4/23/16.
  */
object ChessScanner {

  def findBoard(image: Mat): Mat = {

    Imgproc.circle(image, new Point(image.width()/2, image.height()/2), 50, new Scalar(0.0, 255.0, 0.0), 5)


    return image
  }

}
