package com.antiparagon.cvexperimenter.chessscanner

import java.io.File

import smile.data.{AttributeDataset, NominalAttribute, NumericAttribute, StringAttribute}
import smile.data.parser.DelimitedTextParser
import smile.neighbor.Neighbor

import scala.collection.mutable.ListBuffer

/**
  * Trains a KNN classifier using the FAST training data. Currently uses the
  * Statistical Machine Intelligence and Learning Engine project.
  * The SMILE library (http://haifengl.github.io/smile/) has a Java and
  * Scala API.
  *
  * Created by wmckay on 2/8/17.
  */
object TrainKnnFastClassifier {

  val TRAINING_DATA = "FastClassifierData5KeyPoints.csv"

  def main(args: Array[String]): Unit = {

    val aAvgX = new NumericAttribute("AvgKeyPointX")
    val aAvgY = new NumericAttribute("AvgKeyPointY")
    val aAvgResp = new NumericAttribute("AvgKeyPointResp")
    val aCoord = new StringAttribute("ChessboardCoord")
    val aSymbol = new NominalAttribute("Symbol")
    val aImage = new StringAttribute("Image")

    val numKeyPoints =  determineNumKeypoints(TRAINING_DATA)
    println(s"Num keypoints: $numKeyPoints")
    if(numKeyPoints < 1) {
      println("Not enough keypoints for classification")
      return
    }

    val keyPointList  = new ListBuffer[NumericAttribute]()
    for(i <- 0 to numKeyPoints) {
      keyPointList += new NumericAttribute(s"KeyPoint$i")
    }

    val parser = new DelimitedTextParser()
    parser.setDelimiter(",")
    parser.setColumnNames(true)
    //val attributes = Array(new Attribute)
    parser.setResponseIndex(aSymbol, 4)
    val attData = parser.parse("FAST Train", new File(TRAINING_DATA))
    //val x: Double[][] = usps.toArray(new double[usps.size()][]);

    println(s"Num Attr: ${attData.attributes.size}")

    for(att <- attData.attributes()) {
      println(s"Attr: ${att.getName}")
    }

  }

  /**
    * Determines the number of key points saved in the training data file.
    *
    * @param datafile
    * @return number of keypoints or -1 if no key points wee found
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
