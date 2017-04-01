package com.antiparagon.cvexperimenter.chessscanner

import java.io.File

import com.antiparagon.cvexperimenter.tools.TextUtils

/**
  * Created by wmckay on 4/1/17.
  */
object FeatureUtils {

  /**
    * Determines the number of key points saved in the training data file.
    *
    * @param datafile
    * @return number of keypoints or -1 if no key points were found
    */
  def determineNumKeypoints(datafile: String): Int = {
    val firstLine = TextUtils.getFirstLine(new File(datafile))
    firstLine match {
      case Some(line) => {
        val parts = line.split(",")
        val numKeyPoints = parts.count(_.toLowerCase.startsWith("keypoint"))
        if(numKeyPoints % 3 != 0) {
          return -1
        }
        return numKeyPoints / 3
      }
      case None => return -1
    }
  }
}
