package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.{Mat, Rect}

/**
  * Factory methods to create ChessPieceFinder.
  *
  * Created by wmckay on 1/2/17.
  */
object ChessPieceFinder {

  def apply(): ChessPieceFinder = {
    new ChessPieceFinder
  }

  def apply(debugImagePrefix: String): ChessPieceFinder = {
    val chessPieceFinder = new ChessPieceFinder
    chessPieceFinder.outputDebugImgs = true
    chessPieceFinder.debugImgPrefix = debugImagePrefix
    //chessPieceFinder.classifier = ChessPieceClassifierFastKnn(debugImagePrefix)
    chessPieceFinder.classifier = ChessPieceClassifierFast(debugImagePrefix)
    chessPieceFinder
  }
}

/**
  * Created by wmckay on 6/12/16.
  */
class ChessPieceFinder {

  /*
    For debugging of the algorithm. Outputs intermediate stage images.
   */
  var outputDebugImgs = false
  // Prefix for debug images
  var debugImgPrefix = "ChessPieceFinder_"
  //var classifier = ChessPieceClassifierFastKnn(debugImgPrefix)
  var classifier: ChessPieceClassifier = ChessPieceClassifierFast(debugImgPrefix)


  def findChessPieces(chessboard: Chessboard, boardImg: Mat): Int = {

    if(chessboard == null || boardImg == null || boardImg.empty()) {
      return 0
    }

    var piecesFound = 0
    val emptyRect = new Rect

    chessboard.getSquares().foreach(square => {

      if(square.rect != emptyRect) {
        val squareImg = new Mat(boardImg, square.rect)

        val (col, row) = chessboard.translateMatrixCoor(square.row, square.column)
        val coorStr = col + row.toString

        classifier.classifyPiece(squareImg, coorStr) match {
          case Some(piece) => {
            square.piece = piece
            piecesFound += 1
          }
          case None =>
        }
      }
    })

    piecesFound
  }
}
