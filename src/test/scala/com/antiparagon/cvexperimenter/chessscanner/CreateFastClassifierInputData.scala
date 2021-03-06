package com.antiparagon.cvexperimenter.chessscanner

import java.io.{File, PrintStream}

import scala.collection.mutable
import scala.io.Source
import scala.util.Random

/**
  * Program to use the test data to create training data. Ensures the same
  * number of instances for each piece.
  *
  * Created by wmckay on 3/2/17.
  */
object CreateFastClassifierInputData {

  val ALL_DATA = "AllFastClassifierData.csv"
  val TEST_DATA = "TestFastClassifierData.csv"
  val TRAINING_DATA = "TrainFastClassifierData.csv"
  val NL = System.lineSeparator

  def main(args: Array[String]): Unit = {

    val numKeyPoints =  FeatureUtils.determineNumKeypoints(ALL_DATA)
    println(s"Num keypoints: $numKeyPoints")
    if(numKeyPoints < 1) {
      println("Not enough keypoints for classification")
      return
    }

    val pieceData = mutable.Map[String, mutable.ListBuffer[Array[String]]]()

    // Drop one to remove the column headers
    for (line <- Source.fromFile(ALL_DATA).getLines.drop(1)) {
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

    var minExamples = 1000000 // A large number for initialization
    pieceData.foreach(entry => {
      println(s"Piece: ${entry._1} - examples ${entry._2.size}")
      if(entry._2.size < minExamples) {
        minExamples = entry._2.size
      }
    })

    val trainingOutput = new PrintStream(new File(TRAINING_DATA))
    outputHeaderRow(numKeyPoints, trainingOutput)

    val testOutput = new PrintStream(new File(TEST_DATA))
    outputHeaderRow(numKeyPoints, testOutput)

    pieceData.foreach(entry => {
      val rows = Random.shuffle(entry._2.toList)
//      rows.foreach(row => {
//        outputDataRow(row, trainingOutput)
//      })

      for(i <- 0 until rows.size) {
        if(i < minExamples) {
          outputDataRow(rows(i), trainingOutput)
        } else {
          outputDataRow(rows(i), testOutput)
        }
      }

      // Output rows not used for training to test with

    })
    trainingOutput.close()
    testOutput.close()
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

}
