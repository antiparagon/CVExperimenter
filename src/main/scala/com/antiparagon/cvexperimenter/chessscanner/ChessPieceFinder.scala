package com.antiparagon.cvexperimenter.chessscanner

import java.io.{File, PrintStream}

import com.antiparagon.cvexperimenter.CVExperimenter
import com.antiparagon.cvexperimenter.tools.ImageTools
import org.opencv.core._
import org.opencv.features2d.{FeatureDetector, Features2d}
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc

/**
  * Created by wmckay on 6/12/16.
  */
object ChessPieceFinder {

  // Function to take an image and a matrix of chess square coordinates

  // Find all pawn coordinates

  // Determine color

  // Find rook coordinates

  // Determine color

  // Find knight coordinates

  // Determine color

  // Find bishop coordinates

  // Determine color

  // Find queen coordinates

  // Determine color

  // Find king coordinates

  // Determine color
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

      determinePiece(keyPointsArray) match {
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
            //Imgproc.cvtColor(squareImg, squareImg, Imgproc.COLOR_BGR2GRAY)
            //Imgproc.threshold(squareImg, squareImg, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU)
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

    return piecesFound
  }

  def determinePiece(keyPoints: Array[KeyPoint]): Option[String] = {
    if(keyPoints.length >= 5) {
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
