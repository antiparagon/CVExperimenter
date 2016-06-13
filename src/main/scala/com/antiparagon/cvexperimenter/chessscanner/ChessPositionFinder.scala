package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.Mat

/**
  * Created by wmckay on 6/12/16.
  */
object ChessPositionFinder {

  /**
    * Takes an image of a chessboard and returns the position in FEN notation. This
    * function assumes that the full image is the chessboard.
    *
    * @param inImg of chessboard
    * @return string FEN position
    */
  def getFenPosition(inImg: Mat): String = {
    "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
  }

}
