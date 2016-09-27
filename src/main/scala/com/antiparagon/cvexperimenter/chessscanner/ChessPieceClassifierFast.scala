package com.antiparagon.cvexperimenter.chessscanner

import java.io.PrintStream

import com.antiparagon.cvexperimenter.tools.ImageTools
import org.opencv.core.{Mat, MatOfKeyPoint, Scalar}
import org.opencv.features2d.{FeatureDetector, Features2d}
import org.opencv.imgcodecs.Imgcodecs

/**
  * Created by wmckay on 9/20/16.
  */
object ChessPieceClassifierFast {

  val features = FeatureDetector.create(FeatureDetector.FAST)

  /**
    * Classifies the chess piece using FAST image detection features.
    *
    * @param inputImg
    * @return Some(Piece symbol) or None
    */
  def classifyPiece(inputImg: Mat, coorStr: String, output: PrintStream): Option[String] = {

    val squareImg = ImageTools.resize(inputImg, 50, 50)

    val keyPointsMat = new MatOfKeyPoint()
    features.detect(squareImg, keyPointsMat)

    Features2d.drawKeypoints(squareImg, keyPointsMat, squareImg, new Scalar(0, 0, 255), Features2d.DRAW_RICH_KEYPOINTS)
    val imgPath = "ChessSquares/" + coorStr + ".png"
    Imgcodecs.imwrite(imgPath, squareImg)

    val keyPoints = keyPointsMat.toArray.sortWith(_.response > _.response).take(10)

    var x = 0.0
    var y = 0.0
    val NL = System.lineSeparator()
    keyPoints.toArray.foreach(kp => {
      println(s"${kp}")
      x += kp.pt.x
      y += kp.pt.y
    })

    if(keyPoints.length >= 5) {

      x = x / keyPoints.length.toDouble
      y = y / keyPoints.length.toDouble
      output.append(coorStr).append(",").append(x.toString).append(",").append(y.toString).append(NL)

      val points = keyPoints.length
      var piece = "P"

      // Black pieces
      if(points == 27) {
        piece = "r"
      } else if(points == 24) {
        piece = "n"
      } else if(points == 36) {
        piece = "b"
      } else if(points == 31) {
        piece = "q"
      } else if(points == 57) {
        piece = "k"
      } else if(points == 32) {
        piece = "b"
      } else if(points == 22) {
        piece = "n"
      } else if(points == 17) {
        piece = "r"
      } else if(points == 20) {
        piece = "p"
      } else if(points == 19) {
        piece = "p"
      }
      // White pieces
      else if(points == 15) {
        piece = "R"
      } else if(points == 38) {
        piece = "N"
      } else if(points == 24) {
        piece = "B"
      } else if(points == 59) {
        piece = "Q"
      } else if(points == 42) {
        piece = "K"
      } else if(points == 40) {
        piece = "B"
      } else if(points == 34) {
        piece = "N"
      } else if(points == 22) {
        piece = "R"
      } else if(points == 32) {
        piece = "P"
      } else if(points == 24) {
        piece = "P"
      } else if(points == 26) {
        piece = "P"
      }

      Some(piece)
    } else {
      None
    }
  }

}
