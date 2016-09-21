package com.antiparagon.cvexperimenter.chessscanner

import java.io.{File, PrintStream}

import com.antiparagon.cvexperimenter.CVExperimenter
import com.antiparagon.cvexperimenter.tools.ImageTools
import org.opencv.core._
import org.opencv.features2d.{FeatureDetector, Features2d}
import org.opencv.imgcodecs.Imgcodecs

/**
  * Created by wmckay on 6/12/16.
  */
object ChessPieceFinder {

  def findChessPieces(chessboard: Chessboard, boardImg: Mat): Int = {

    import scala.collection.JavaConverters._

    var piecesFound = 0
    val NL = System.getProperty("line.separator")
    var output: PrintStream = null

    if(chessboard == null || boardImg == null || boardImg.empty()) {
      return 0
    }

    if(CVExperimenter.OUTPUT_PIECE_FEATURES) {
      output = new PrintStream(new File("foundpieces.csv"))
      output.append("Square").append(",").append("X").append(",").append("Y").append(",").append("Response").append(",").append("Piece").append(NL)
    }

    val features = FeatureDetector.create(FeatureDetector.FAST)

    var minKp = 10000.0
    var maxKp = 0.0

    chessboard.getSquares().foreach(square => {
      val squareImg = new Mat(boardImg, square.rect)

      val keyPointsMat = new MatOfKeyPoint()
      features.detect(squareImg, keyPointsMat)

      val keyPointsArray = keyPointsMat.toArray.sortWith(_.response > _.response).take(10)

      val (col, row) = chessboard.translateMatrixCoor(square.row, square.column)
      val coorStr = col + row.toString

      println(s"$coorStr: ${keyPointsArray.length}")

      ChessPieceClassifier.classifyPiece(keyPointsArray) match {
        case Some(piece) => {

          if(minKp > keyPointsArray.length) minKp = keyPointsArray.length
          if(maxKp < keyPointsArray.length) maxKp = keyPointsArray.length

          square.piece = piece
          piecesFound += 1
          if(CVExperimenter.OUTPUT_PIECE_FEATURES && output != null) {
            keyPointsArray.foreach(kp => {
              println(s"${kp}")
              output.append(coorStr).append(",").append(kp.pt.x.toString).append(",").append(kp.pt.y.toString).append(",").append(kp.response.toString).append(",").append(square.piece).append(NL)
            })
          }

          if(CVExperimenter.OUTPUT_PIECE_FEATURES) {
            val matOfKeyPoints = new MatOfKeyPoint()
            matOfKeyPoints.fromList(keyPointsArray.toList.asJava)
            Features2d.drawKeypoints(squareImg, matOfKeyPoints, squareImg, new Scalar(0, 0, 255), Features2d.DRAW_RICH_KEYPOINTS)

            val result = ImageTools.resize(squareImg, 3.0)

            val imgPath = "ChessSquares/" + coorStr + ".png"
            Imgcodecs.imwrite(imgPath, result)

            println(s"Min keypoints: $minKp")
            println(s"Max keypoints: $maxKp")
          }
        }
        case None =>
      }
    })

    if(CVExperimenter.OUTPUT_PIECE_FEATURES && output != null) {
      output.close()
    }

    piecesFound
  }

}
