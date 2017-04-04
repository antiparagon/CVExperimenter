package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.{KeyPoint, Mat}

/**
  * Created by wmckay on 4/2/17.
  */
trait ChessPieceClassifier {

  case class FeatureScore(avgX: Double, avgY: Double, avgResp: Double, keyPoints: Array[KeyPoint])
  val scores = scala.collection.mutable.Map[String, FeatureScore]()

  val numKeyPoints = 0

  def classifyPiece(inputImg: Mat, coorStr: String): Option[String]

}
