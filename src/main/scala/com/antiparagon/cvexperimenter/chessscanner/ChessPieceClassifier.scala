package com.antiparagon.cvexperimenter.chessscanner

import com.antiparagon.cvexperimenter.CVExperimenter
import com.antiparagon.cvexperimenter.tools.ImageTools
import org.opencv.core.{KeyPoint, Mat, MatOfKeyPoint, Scalar}
import org.opencv.features2d.Features2d
import org.opencv.imgcodecs.Imgcodecs


/**
  * Created by wmckay on 9/20/16.
  */
object ChessPieceClassifier {



  def classifyPiece(squareImg: Mat, keyPointsMat: MatOfKeyPoint): Option[String] = {

    val keyPoints = keyPointsMat.toArray.sortWith(_.response > _.response).take(10)


    if(keyPoints.length >= 5) {

//      if(CVExperimenter.OUTPUT_PIECE_FEATURES && output != null) {
//        keyPoints.foreach(kp => {
//          println(s"${kp}")
//          output.append(coorStr).append(",").append(kp.pt.x.toString).append(",").append(kp.pt.y.toString).append(",").append(kp.response.toString).append(",").append(square.piece).append(NL)
//        })
//      }
//
//      if(CVExperimenter.OUTPUT_PIECE_FEATURES) {
//        val matOfKeyPoints = new MatOfKeyPoint()
//        matOfKeyPoints.fromList(keyPointsArray.toList.asJava)
//        Features2d.drawKeypoints(squareImg, matOfKeyPoints, squareImg, new Scalar(0, 0, 255), Features2d.DRAW_RICH_KEYPOINTS)
//
//        val result = ImageTools.resize(squareImg, 3.0)
//
//        val imgPath = "ChessSquares/" + coorStr + ".png"
//        Imgcodecs.imwrite(imgPath, result)
//
//        println(s"Min keypoints: $minKp")
//        println(s"Max keypoints: $maxKp")
//      }


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
