package com.antiparagon.cvexperimenter.chessscanner

import java.io.File
import java.nio.file.{Files, Path, Paths, StandardCopyOption}

import scala.collection.mutable
import scala.io.Source


/**
  * Program to modify the training data created to only have the same
  * number of instances for each piece.
  *
  * Created by wmckay on 3/2/17.
  */
object ModifyFastClassifierTrainingData {

  val TRAINING_DATA = "FastClassifierData.csv"

  def main(args: Array[String]): Unit = {

    val numKeyPoints =  determineNumKeypoints(TRAINING_DATA)
    println(s"Num keypoints: $numKeyPoints")
    if(numKeyPoints < 1) {
      println("Not enough keypoints for classification")
      return
    }

    val pieceData = mutable.Map[String, mutable.ListBuffer[Array[String]]]()

    // Drop one to remove the column headers
    for (line <- Source.fromFile(TRAINING_DATA).getLines.drop(1)) {
      println(line)
      val data = line.split(",")
      if(data.length != (numKeyPoints * 3) + 4) {
        println("Unable to parse trainging data correctly")
        return
      }
      val piece = data(3)
      println(piece)
      if(!pieceData.contains(piece)) {
        pieceData += (piece -> mutable.ListBuffer())
      }
      pieceData(piece) += data
    }

    pieceData.foreach(entry => {
      println(s"Piece: ${entry._1} - examples ${entry._2.size}")
    })
  }

  /**
    * Determines the number of key points saved in the training data file.
    *
    * @param datafile
    * @return number of keypoints or -1 if no key points were found
    */
  def determineNumKeypoints(datafile: String): Int = {
    val firstLine = getFirstLine(new File(datafile))
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

  /**
    * Helper function that returns the first line of a file.
    *
    * @param file
    * @return first line of a file or None
    */
  def getFirstLine(file: java.io.File): Option[String] = {
    val src = io.Source.fromFile(file)
    try {
      src.getLines.find(_ => true)
    } finally {
      src.close()
    }
  }

}
