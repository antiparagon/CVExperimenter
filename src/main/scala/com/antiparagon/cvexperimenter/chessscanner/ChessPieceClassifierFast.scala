package com.antiparagon.cvexperimenter.chessscanner

import com.antiparagon.cvexperimenter.tools.ImageTools
import org.opencv.core.{KeyPoint, Mat, MatOfKeyPoint, Scalar}
import org.opencv.features2d.{FeatureDetector, Features2d}
import org.opencv.imgcodecs.Imgcodecs


case class FeatureScoreFast(avgX: Double, avgY: Double, avgResp: Double, keyPoints: Array[KeyPoint])

/**
  * Created by wmckay on 1/4/17.
  */
object ChessPieceClassifierFast {

  def apply(): ChessPieceClassifierFast = {
    new ChessPieceClassifierFast
  }

  def apply(debugImagePrefix: String): ChessPieceClassifierFast = {
    val chessPieceClassifierFast = new ChessPieceClassifierFast
    chessPieceClassifierFast.outputDebugImgs = true
    chessPieceClassifierFast.debugImgPrefix = debugImagePrefix //+ chessPieceClassifierFast.debugImgPrefix
    chessPieceClassifierFast
  }
}

/**
  * Created by wmckay on 9/20/16.
  */
class ChessPieceClassifierFast extends ChessPieceClassifier {

  val features = FeatureDetector.create(FeatureDetector.FAST)
  val scores = scala.collection.mutable.Map[String, FeatureScoreFast]()
  override val numKeyPoints = 15

  /*
    For debugging of the algorithm. Outputs intermediate stage images.
   */
  var outputDebugImgs = false
  // Prefix for debug images
  var debugImgPrefix = "ChessPieceClassifierFast"

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

      scores += (coorStr -> FeatureScoreFast(x, y, resp, keyPoints))

      var piece = "X"

      Some(piece)
    } else {
      None
    }
  }
}
