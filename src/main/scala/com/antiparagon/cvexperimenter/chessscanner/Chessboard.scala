package com.antiparagon.cvexperimenter.chessscanner

/**
  * Created by wmckay on 6/21/16.
  */
class Chessboard {

  val rows = 8
  val columns = 8
  // The '+1' is so that indexing can be 1 based instead of 0 based
  val board = Array.ofDim[ChessSquare](rows + 1, columns + 1)

  for(row <- 1 to rows) {
    for(column <- 1 to columns) {
      board(row)(column) = new ChessSquare
    }
  }

  /**
    * Clears all the squares. Results in an empty board.
    */
  def clearBoard() = {
    for(row <- 1 to rows) {
      for(column <- 1 to columns) {
        board(row)(column).clear
      }
    }
  }

  def setStartPosition() = {
    board(1)(1).symbol = "R"

  }


  /**
    * Returns the position in FEN notation.
    *
    * @return Option string FEN position
    */
  def getFenPosition(): Option[String] = {

    val buf = new StringBuilder
    for(row <- 1 to rows) {
      for(column <- 1 to columns) {

        if(board(row)(column).symbol.equals("")) {

        }
        buf ++= board(row)(column).symbol
      }
      buf += '/'
    }

    // Use the position to create the FEN string

    Option(buf.toString())
  }

}
