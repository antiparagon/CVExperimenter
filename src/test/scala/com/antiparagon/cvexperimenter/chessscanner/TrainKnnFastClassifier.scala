package com.antiparagon.cvexperimenter.chessscanner

import java.io.File

import smile.classification.KNN
import smile.data.{Attribute, AttributeDataset, NominalAttribute, NumericAttribute, StringAttribute}
import smile.data.parser.DelimitedTextParser
import smile.neighbor.Neighbor

import scala.collection.mutable.ArrayBuffer

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

    val attributeBuffer  = new ArrayBuffer[Attribute]()
    val aAvgX = new NumericAttribute("AvgKeyPointX")
    attributeBuffer += aAvgX
    val aAvgY = new NumericAttribute("AvgKeyPointY")
    attributeBuffer += aAvgY
    val aAvgResp = new NumericAttribute("AvgKeyPointResp")
    attributeBuffer += aAvgResp
    val aCoord = new StringAttribute("ChessboardCoord")
    attributeBuffer += aCoord
    val aSymbol = new NominalAttribute("Symbol")
    //attributeBuffer += aSymbol
    val aImage = new StringAttribute("Image")
    attributeBuffer += aImage

    val numKeyPoints =  determineNumKeypoints(TRAINING_DATA)
    println(s"Num keypoints: $numKeyPoints")
    if(numKeyPoints < 1) {
      println("Not enough keypoints for classification")
      return
    }

    // Add the number of key points found by parsing the training file
    for(i <- 1 to numKeyPoints) {
      attributeBuffer += new NumericAttribute(s"KeyPoint${i}X")
      attributeBuffer += new NumericAttribute(s"KeyPoint${i}Y")
      attributeBuffer += new NumericAttribute(s"KeyPoint${i}Resp")
    }

    println(s"attributeBuffer length: ${attributeBuffer.length}")

    val parser = new DelimitedTextParser()
    parser.setDelimiter(",")
    parser.setColumnNames(true)
    //val attributes = Array(new Attribute)
    parser.setResponseIndex(aSymbol, 4)
    val attData = parser.parse("FAST Train", attributeBuffer.toArray, new File(TRAINING_DATA))
    val x  = attData.toArray(new Array[Array[Double]](attData.size()))
    //attData.setResponseIndex(4)
    //val x = attData.toArray(new Double(0)())
    val y = attData.toArray(new Array[Int](attData.size()))

//    outputClasses(aSymbol, y)
//    for(att <- attData.attributes()) {
//      println(s"Attr: ${att.getName}")
//    }

    val knn = KNN.learn(x, y, 1)
    var right = 0
    var total = 0
    for(i <- 0 until attData.size()) {
      val predict = knn.predict(x(i))
      val correct = x(i)(4)
      if(predict == correct) right += 1
      total += 1
      println(s"Predict: ${aSymbol.toString(predict)} - ${aSymbol.toString(correct)}")
    }
    println(s"Correct predict: $right of $total")

    //println(s"Num Attr: ${attData.attributes.size}")
    //val response = attData.response()
    //println(s"Response: ${response.getName}")



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

  /**
    * Function to output the classes determined by the parser.
    * @param classses
    */
  def outputClasses(attribute: Attribute, classses: Array[Int]): Unit = {
      for(clazz <- classses) {
        println(s"Class: ${attribute.toString(clazz)}")
      }
  }
}
