package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.{Mat, Rect}
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

    chessboard.getSquares().foreach(square => {
      val squareImg = new Mat(boardImg, square.rect)
      val (col, row) = chessboard.translateMatrixCoor(square.row, square.column)
      val coorStr = col + row.toString
      val imgPath = "ChessSquares/" + coorStr + ".png"
      println(imgPath)
      Imgcodecs.imwrite(imgPath, squareImg)
    })

    return piecesFound
  }


}
