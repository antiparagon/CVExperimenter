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

  def getPiece(column: String, row: Int): String = {

    if(column.isEmpty) {
      return ""
    }
    val colChar = column.toLowerCase.charAt(0);
    if(colChar < 97 || colChar > 104) {
      return ""
    }
    if(row < 1 || row > 8) {
      return ""
    }
    val col = colChar - 96
    val flippedRow = row match {
      case 1  => 8
      case 2  => 7
      case 3  => 6
      case 4  => 5
      case 5  => 4
      case 6  => 3
      case 7  => 2
      case 8  => 1
    }
    board(flippedRow)(col).symbol
  }

  /**
    * Initialize the board to the starting position.
    */
  def setStartPosition() = {
    board(1)(1).symbol = "r"
    board(1)(2).symbol = "n"
    board(1)(3).symbol = "b"
    board(1)(4).symbol = "q"
    board(1)(5).symbol = "k"
    board(1)(6).symbol = "b"
    board(1)(7).symbol = "n"
    board(1)(8).symbol = "r"
    for(column <- 1 to columns) board(2)(column).symbol = "p"

    for(column <- 1 to columns) board(7)(column).symbol = "P"
    board(8)(1).symbol = "R"
    board(8)(2).symbol = "N"
    board(8)(3).symbol = "B"
    board(8)(4).symbol = "Q"
    board(8)(5).symbol = "K"
    board(8)(6).symbol = "B"
    board(8)(7).symbol = "N"
    board(8)(8).symbol = "R"
  }


  /**
    * Returns the position in FEN notation.
    *
    * @return Option string FEN position
    */
  def getFenPosition(): Option[String] = {

    val buf = new StringBuilder
    for(row <- 1 to rows) {
      var empty = 0
      for(column <- 1 to columns) {
        if(board(row)(column).isEmpty) {
          empty += 1
        } else {
          if(empty > 0) {
            buf ++= empty.toString
            empty = 0
          }
          buf ++= board(row)(column).symbol
        }
      }
      if(empty > 0) { // Catch the case when the whole row is empty
        buf ++= empty.toString
      }
      if(row < rows) buf += '/'
    }

    // Use the position to create the FEN string

    Option(buf.toString())
  }

}
