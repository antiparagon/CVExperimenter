package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.Mat


/**
  * Created by wmckay on 6/12/16.
  */
object ChessPieceFinder {

  def findChessPieces(chessboard: Chessboard, boardImg: Mat): Int = {

    var piecesFound = 0

    if(chessboard == null || boardImg == null || boardImg.empty()) {
      return 0
    }

    chessboard.getSquares().foreach(square => {
      val squareImg = new Mat(boardImg, square.rect)

      ChessPieceClassifierFast.classifyPiece(squareImg) match {
        case Some(piece) => {
          square.piece = piece
          piecesFound += 1
        }
        case None =>
      }
    })

    piecesFound
  }

}
