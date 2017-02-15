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
  val numKeyPoints = 3

  def main(args: Array[String]): Unit = {

    val aAvgX = new NumericAttribute("AvgKeyPointX")
    val aAvgY = new NumericAttribute("AvgKeyPointY")
    val aAvgResp = new NumericAttribute("AvgKeyPointResp")
    val aCoord = new StringAttribute("ChessboardCoord")
    val aSymbol = new NominalAttribute("Symbol")
    val aImage = new StringAttribute("Image")

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

  def determineNumKeypoints(datafile: String): Int = {


    return -1
  }

  def firstLine(f: java.io.File): Option[String] = {
    val src = io.Source.fromFile(f)
    try {
      src.getLines.find(_ => true)
    } finally {
      src.close()
    }
  }
}
