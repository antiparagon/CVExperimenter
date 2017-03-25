package com.antiparagon.cvexperimenter.chessscanner

import java.io.File

import com.antiparagon.cvexperimenter.tools.ImageTools
import org.opencv.core.{KeyPoint, Mat, MatOfKeyPoint}
import org.opencv.features2d.FeatureDetector
import org.opencv.imgcodecs.Imgcodecs
import smile.classification.KNN
import smile.data.{Attribute, NominalAttribute, NumericAttribute}
import smile.data.parser.DelimitedTextParser

import scala.collection.mutable.ArrayBuffer


case class FeatureScoreFastKnn(avgX: Double, avgY: Double, avgResp: Double, keyPoints: Array[KeyPoint])

/**
  * Created by wmckay on 3/17/17.
  */
object ChessPieceClassifierFastKnn {

  def apply(): ChessPieceClassifierFastKnn = {
    new ChessPieceClassifierFastKnn
  }

  def apply(debugImagePrefix: String): ChessPieceClassifierFastKnn = {
    val chessPieceClassifierFastKnn = new ChessPieceClassifierFastKnn
    chessPieceClassifierFastKnn.outputDebugImgs = true
    chessPieceClassifierFastKnn.debugImgPrefix = debugImagePrefix //+ chessPieceClassifierFastKnn.debugImgPrefix
    chessPieceClassifierFastKnn
  }
}

/**
  * Created by wmckay on 3/17/16.
  */
class ChessPieceClassifierFastKnn {

  val features = FeatureDetector.create(FeatureDetector.FAST)
  val scores = scala.collection.mutable.Map[String, FeatureScoreFastKnn]()

  val NUM_NEIGHBORS = 2
  val TRAINING_DATA = "TrainFastClassifierData.csv"
  val attributeBuffer  = new ArrayBuffer[Attribute]()
  val aAvgX = new NumericAttribute("AvgKeyPointX")
  attributeBuffer += aAvgX
  val aAvgY = new NumericAttribute("AvgKeyPointY")
  attributeBuffer += aAvgY
  val aAvgResp = new NumericAttribute("AvgKeyPointResp")
  attributeBuffer += aAvgResp
  // Response attribute
  val aSymbol = new NominalAttribute("Symbol")

  val numKeyPoints =  determineNumKeypoints(TRAINING_DATA)
  println(s"Num keypoints: $numKeyPoints")
  if(numKeyPoints < 1) {
    println("Not enough keypoints for classification")
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

  /*
    For debugging of the algorithm. Outputs intermediate stage images.
   */
  var outputDebugImgs = false
  // Prefix for debug images
  var debugImgPrefix = "ChessPieceClassifierFastKnn"

  /**
    * Classifies the chess piece using FAST image detection features. The inputImg
    * is an individual chess square with/without a chess piece.
    *
    * @param inputImg
    * @return Some(Piece symbol) or None
    */
  def classifyPiece(inputImg: Mat, coorStr: String): Option[String] = {

    val squareImg = ImageTools.resize(inputImg, 50, 50)

    val keyPointsMat = new MatOfKeyPoint()
    features.detect(squareImg, keyPointsMat)

    val keyPoints = keyPointsMat.toArray.sortWith(_.response > _.response).take(numKeyPoints)

    var x = 0.0
    var y = 0.0
    var resp = 0.0
    keyPoints.foreach(kp => {
      x += kp.pt.x
      y += kp.pt.y
      resp += kp.response.toDouble
    })

    if(keyPoints.length >= 5) {

      if(outputDebugImgs) {
        //val bestKeyPoints: MatOfKeyPoint = new MatOfKeyPoint(keyPoints: _*)
        //Features2d.drawKeypoints(squareImg, bestKeyPoints, squareImg, new Scalar(0, 0, 255), Features2d.DRAW_RICH_KEYPOINTS)
        val imgPath = "ChessSquares/" + coorStr + "_" + debugImgPrefix + ".png"
        Imgcodecs.imwrite(imgPath, squareImg)
      }

      x = x / keyPoints.length.toDouble
      y = y / keyPoints.length.toDouble
      resp = resp / keyPoints.length.toDouble

      val features = toDoubleArray(FeatureScoreFastKnn(x, y, resp, keyPoints))

      val predict = knn.predict(features)
      var piece = aSymbol.toString(predict)

      Some(piece)
    } else {
      None
    }
  }

  /**
    * Converts a FeatureScoreFastKnn into Array[Double].
    *
    * @param featureScore to convert
    * @return Array[Double]
    */
  def toDoubleArray(featureScore: FeatureScoreFastKnn): Array[Double] = {
    val doubleBuffer = scala.collection.mutable.ArrayBuffer[Double]()
    doubleBuffer += featureScore.avgX
    doubleBuffer += featureScore.avgY
    doubleBuffer += featureScore.avgResp
    featureScore.keyPoints.foreach(keyPoint => {
      doubleBuffer += keyPoint.pt.x
      doubleBuffer += keyPoint.pt.y
      doubleBuffer += keyPoint.response
    })
    doubleBuffer.toArray
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
