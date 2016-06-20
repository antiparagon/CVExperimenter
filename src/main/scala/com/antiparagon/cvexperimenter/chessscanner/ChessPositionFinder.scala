package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.Mat

/**
  * Created by wmckay on 6/12/16.
  */
object ChessPositionFinder {

  /**
    * Takes an image of a chessboard and returns the position in FEN notation. This
    * function assumes that the full image is just the chessboard.
    *
    * @param inImg of chessboard
    * @return Option string FEN position
    */
  def getFenPosition(inImg: Mat): Option[String] = {

    // Get square coordinates, find all 64 squares


    // Find all the pieces


    // Use the squares coordinates to determine the square the pieces are on


    // Use the position to create the FEN string


    Option("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
  }

}
