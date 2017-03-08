package com.antiparagon.cvexperimenter.chessscanner

import java.io.{File, PrintStream}
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
  val MOD_TRAINING_DATA = "ModFastClassifierData.csv"
  val NL = System.lineSeparator

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
      val data = line.split(",")
      if(data.length != (numKeyPoints * 3) + 4) {
        println("Unable to parse training data correctly")
        return
      }
      val piece = data(3)
      if(!pieceData.contains(piece)) {
        pieceData += (piece -> mutable.ListBuffer())
      }
      pieceData(piece) += data
    }

    pieceData.foreach(entry => {
      println(s"Piece: ${entry._1} - examples ${entry._2.size}")
    })

    val output = new PrintStream(new File(MOD_TRAINING_DATA))
    outputHeaderRow(numKeyPoints, output)
    pieceData.foreach(entry => {
      entry._2.foreach(row => {
        outputDataRow(row, output)
      })
    })
    output.close()
  }


  /**
    * Writes a row to the output.
    *
    * @param data a row of data
    * @param output PrintStream to output to
    */
  def outputDataRow(data: Array[String], output: PrintStream): Unit = {

    for(i <- 0 until (data.length - 1)) {
      output.append(data(i)).append(",")
    }
    output.append(data(data.length - 1)).append(NL)
  }

  /**
    * Writes the CSV header row to the output.
    *
    * @param numKeyPoints number of key points to output
    * @param output PrintStream to output to
    */
  def outputHeaderRow(numKeyPoints: Int, output: PrintStream): Unit = {
    output.append("AvgKeyPointX").append(",").append("AvgKeyPointY").append(",").append("AvgKeyPointResp").append(",").append("Symbol")
    // Add key point headers
    for(i <- 1 to numKeyPoints) {
      output.append(",").append(s"KeyPoint${i}X").append(",").append(s"KeyPoint${i}Y").append(",").append(s"KeyPoint${i}Resp")
    }
    output.append(NL)
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
