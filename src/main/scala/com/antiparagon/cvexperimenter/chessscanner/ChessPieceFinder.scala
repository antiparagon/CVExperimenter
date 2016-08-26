package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.{Mat, MatOfKeyPoint, Rect, Scalar}
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
    var piecesFound = 0

    val features = FeatureDetector.create(FeatureDetector.FAST)
    chessboard.getSquares().foreach(square => {
      val squareImg = new Mat(boardImg, square.rect)

      val keyPoints = new MatOfKeyPoint()
      features.detect(squareImg, keyPoints)
      Features2d.drawKeypoints(squareImg, keyPoints, squareImg, new Scalar(0, 0, 255), Features2d.DRAW_RICH_KEYPOINTS)

      val (col, row) = chessboard.translateMatrixCoor(square.row, square.column)
      val coorStr = col + row.toString

      println(s"$coorStr: ${keyPoints.size()}")

      if(keyPoints.size().height > 5) {
        square.piece = "p"
        piecesFound += 1
      }


      val imgPath = "ChessSquares/" + coorStr + ".png"
      Imgcodecs.imwrite(imgPath, squareImg)
    })

    return piecesFound
  }


}
