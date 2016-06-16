package com.antiparagon.cvexperimenter.chessscanner

import java.util

import org.opencv.core._
import org.opencv.imgproc.Imgproc


/**
  * Created by wmckay on 6/16/16.
  */
object ChessScanner {

  def getFenPosition(inImg: Mat): Option[String] = {

    val boardImg = ChessBoardFinder.getChessboard(inImg)

    // Unable to find a rectangle that has a chessboard
    if(boardImg.isEmpty) {
      return None
    }

    

    Option("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
  }

}
