package com.antiparagon.cvexperimenter.chessscanner

/**
  * Created by wmckay on 6/21/16.
  */
class Chessboard {

  val rows = 8
  val columns = 8
  val board = Array.ofDim[ChessSquare](rows, columns)

  /**
    * Returns the position in FEN notation.
    *
    * @return Option string FEN position
    */
  def getFenPosition(): Option[String] = {

    // Use the position to create the FEN string


    Option("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
  }

}
