package com.antiparagon.cvexperimenter.chessscanner

import java.io.File

import smile.classification.KNN
import smile.data.{Attribute, NominalAttribute, NumericAttribute}
import smile.data.parser.DelimitedTextParser

import scala.collection.mutable
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

  val TRAINING_DATA = "TrainFastClassifierData.csv"
  val TEST_DATA = "TestFastClassifierData.csv"
  val NUM_NEIGHBORS = 2

  def main(args: Array[String]): Unit = {

    val attributeBuffer  = new ArrayBuffer[Attribute]()
    val aAvgX = new NumericAttribute("AvgKeyPointX")
    attributeBuffer += aAvgX
    val aAvgY = new NumericAttribute("AvgKeyPointY")
    attributeBuffer += aAvgY
    val aAvgResp = new NumericAttribute("AvgKeyPointResp")
    attributeBuffer += aAvgResp
    // Response attribute
    val aSymbol = new NominalAttribute("Symbol")


    val numKeyPoints =  FeatureUtils.determineNumKeypoints(TRAINING_DATA)
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


    val trainingParser = new DelimitedTextParser()
    trainingParser.setDelimiter(",")
    trainingParser.setColumnNames(true)
    trainingParser.setResponseIndex(aSymbol, 3)
    val trainingAttData = trainingParser.parse("FAST Train", attributeBuffer.toArray, new File(TRAINING_DATA))

    val trainingX  = trainingAttData.toArray(new Array[Array[Double]](trainingAttData.size()))
    val trainingY = trainingAttData.toArray(new Array[Int](trainingAttData.size()))


    val knn = KNN.learn(trainingX, trainingY, NUM_NEIGHBORS)


    // Load in the test data
    val testParser = new DelimitedTextParser
    testParser.setDelimiter(",")
    testParser.setColumnNames(true)
    testParser.setResponseIndex(aSymbol, 3)
    val testAttData = testParser.parse("FAST Test", attributeBuffer.toArray, new File(TEST_DATA))

    val testX  = testAttData.toArray(new Array[Array[Double]](testAttData.size))
    val testY = testAttData.toArray(new Array[Int](testAttData.size))


    val totalMap = mutable.Map[String, Int]()
    val correctMap = mutable.Map[String, Int]()
    val wrongMap = mutable.Map[String, Int]()

    var right = 0
    var total = 0
    for(i <- 0 until testAttData.size) {

      val predict = knn.predict(testX(i))
      val correct = testY(i)

      if(!(totalMap contains aSymbol.toString(correct))) {
        totalMap += (aSymbol.toString(correct) -> 0)
      }
      totalMap(aSymbol.toString(correct)) = totalMap(aSymbol.toString(correct))  + 1

      if(predict == correct) {
        right += 1
        if(!(correctMap contains aSymbol.toString(correct))) {
          correctMap += (aSymbol.toString(correct) -> 0)
        }
        correctMap(aSymbol.toString(correct)) = correctMap(aSymbol.toString(correct))  + 1
      } else {
        if(!(wrongMap contains aSymbol.toString(correct))) {
          wrongMap += (aSymbol.toString(correct) -> 0)
        }
        wrongMap(aSymbol.toString(correct)) = wrongMap(aSymbol.toString(correct))  + 1
      }
      total += 1
      println(s"Predict: ${aSymbol.toString(predict)} - ${aSymbol.toString(correct)}")
    }
    println(s"Correct prediction: $right of $total")
    correctMap.foreach(entry => {
      println(s"${entry._1} - ${entry._2}")
    })
    println(s"Wrong prediction: ${total - right} of $total")
    wrongMap.foreach(entry => {
      println(s"${entry._1} - ${entry._2}")
    })
    println(s"Total: $total")
    totalMap.foreach(entry => {
      println(s"${entry._1} - ${entry._2}")
    })

  }

}
