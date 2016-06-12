package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.Mat

/**
  * Created by wmckay on 6/12/16.
  */
object ChessPositionFinder {

  def getFenPosition(inImg: Mat): String = {
    "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
  }

}
