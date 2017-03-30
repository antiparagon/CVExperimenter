package com.antiparagon.cvexperimenter.chessscanner

import java.io.File

import smile.classification.{KNN, LDA, QDA}
import smile.data.parser.DelimitedTextParser
import smile.data.{Attribute, NominalAttribute, NumericAttribute}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Trains a QDA classifier using the FAST training data. Currently uses the
  * Statistical Machine Intelligence and Learning Engine project.
  * The SMILE library (http://haifengl.github.io/smile/) has a Java and
  * Scala API.
  *
  * Created by wmckay on 3/26/17.
  */
object TrainQdaFastClassifierNew {

  val TRAINING_DATA = "TrainFastClassifierDataNew.csv"
  val TEST_DATA = "TestFastClassifierDataNew.csv"

  def main(args: Array[String]): Unit = {

    val attributeBuffer  = new ArrayBuffer[Attribute]()
    // Response attribute
    val aSymbol = new NominalAttribute("Symbol")


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


    val trainingParser = new DelimitedTextParser()
    trainingParser.setDelimiter(",")
    trainingParser.setColumnNames(true)
    trainingParser.setResponseIndex(aSymbol, 0)
    val trainingAttData = trainingParser.parse("FAST Train", attributeBuffer.toArray, new File(TRAINING_DATA))

    val trainingX  = trainingAttData.toArray(new Array[Array[Double]](trainingAttData.size()))
    val trainingY = trainingAttData.toArray(new Array[Int](trainingAttData.size()))


    //val knn = KNN.learn(trainingX, trainingY, NUM_NEIGHBORS)
    val qda = new QDA(trainingX, trainingY, null, 0.00000000000000001)


    // Load in the test data
    val testParser = new DelimitedTextParser
    testParser.setDelimiter(",")
    testParser.setColumnNames(true)
    testParser.setResponseIndex(aSymbol, 0)
    val testAttData = testParser.parse("FAST Test", attributeBuffer.toArray, new File(TEST_DATA))

    val testX  = testAttData.toArray(new Array[Array[Double]](testAttData.size))
    val testY = testAttData.toArray(new Array[Int](testAttData.size))


    val totalMap = mutable.Map[String, Int]()
    val correctMap = mutable.Map[String, Int]()
    val wrongMap = mutable.Map[String, Int]()

    var right = 0
    var total = 0
    val posteriori = new Array[Double](3)
    for(i <- 0 until testAttData.size) {


      for(j <- 0 until testX(i).length) {
        System.out.print(s"${testX(i)(j)} ")
      }
      System.out.println()


      val predict = qda.predict(testX(i), posteriori)
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
