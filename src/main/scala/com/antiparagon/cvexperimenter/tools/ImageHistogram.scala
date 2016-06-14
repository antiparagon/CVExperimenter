package com.antiparagon.cvexperimenter.tools

import java.util

import org.opencv.core._
import org.opencv.imgproc.Imgproc

/**
  * Utility to create a histogram of an image.
  *
  * Created by wmckay on 5/28/16.
  */
object ImageHistogram {

  def createHistogram(frame: Mat, gray: Boolean): Mat =  {
    // split the frames in multiple images
    val images = new util.ArrayList[Mat]
    Core.split(frame, images)

    // set the number of bins at 256
    val histSize = new MatOfInt(256)
    // only one channel
    val channels = new MatOfInt(0)
    // set the ranges
    val histRange = new MatOfFloat(0, 256)

    // compute the histograms for the B, G and R components
    val hist_b = new Mat()
    val hist_g = new Mat()
    val hist_r = new Mat()

    // B component or gray image
    Imgproc.calcHist(images.subList(0, 1), channels, new Mat(), hist_b, histSize, histRange, false)

    // G and R components (if the image is not in gray scale)
    if (!gray) {
      Imgproc.calcHist(images.subList(1, 2), channels, new Mat(), hist_g, histSize, histRange, false)
      Imgproc.calcHist(images.subList(2, 3), channels, new Mat(), hist_r, histSize, histRange, false)
    }

    // draw the histogram
    val hist_w = 256 // width of the histogram image
    val hist_h = 150 // height of the histogram image
    val bin_w = Math.round(hist_w / histSize.get(0, 0)(0)).toInt

    val histImage = new Mat(hist_h, hist_w, CvType.CV_8UC3, new Scalar(0, 0, 0))
    // normalize the result to [0, histImage.rows()]
    Core.normalize(hist_b, hist_b, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat())

    // for G and R components
    if (!gray) {
      Core.normalize(hist_g, hist_g, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat())
      Core.normalize(hist_r, hist_r, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat())
    }

    // effectively draw the histogram(s)
    var i = 0
    for (i <- 1 until (histSize.get(0, 0)(0)).toInt) {

      // B component or gray image
      Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(hist_b.get(i - 1, 0)(0))),
      new Point(bin_w * (i), hist_h - Math.round(hist_b.get(i, 0)(0))), new Scalar(255, 0, 0), 2, 8, 0);
      // G and R components (if the image is not in gray scale)
      if (!gray) {
        Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(hist_g.get(i - 1, 0)(0))),
        new Point(bin_w * (i), hist_h - Math.round(hist_g.get(i, 0)(0))), new Scalar(0, 255, 0), 2, 8, 0);
        Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(hist_r.get(i - 1, 0)(0))),
        new Point(bin_w * (i), hist_h - Math.round(hist_r.get(i, 0)(0))), new Scalar(0, 0, 255), 2, 8, 0);
      }
    }

    return histImage

  }

}
