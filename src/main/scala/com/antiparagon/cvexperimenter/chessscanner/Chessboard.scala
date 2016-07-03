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

  /**
    * Sets the piece at on the square specified in algebraic notation.
    *
    * @param column to use
    * @param row to use
    * @param piece to use
    */
  def setPiece(column: String, row: Int, piece: String): Unit = {
    if(column.isEmpty) {
      return
    }
    val colChar = column.toLowerCase.charAt(0);
    if(colChar < 97 || colChar > 104) {
      return
    }
    if(row < 1 || row > 8) {
      return
    }
    val col = colChar - 96
    val flippedRow = 9 - row
    board(flippedRow)(col).piece = piece
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
    val flippedRow = 9 - row
    board(flippedRow)(col).piece
  }

  def isWhite(column: String, row: Int): Boolean = {
    if(column.isEmpty) {
      return false
    }
    val colChar = column.toLowerCase.charAt(0);
    if(colChar < 97 || colChar > 104) {
      return false
    }
    if(row < 1 || row > 8) {
      return false
    }
    val col = colChar - 96
    val flippedRow = 9 - row
    board(flippedRow)(col).isWhite()
  }

  /**
    * Initialize the board to the starting position.
    */
  def setStartPosition() = {
    board(1)(1).piece = "r"
    board(1)(2).piece = "n"
    board(1)(3).piece = "b"
    board(1)(4).piece = "q"
    board(1)(5).piece = "k"
    board(1)(6).piece = "b"
    board(1)(7).piece = "n"
    board(1)(8).piece = "r"
    for(column <- 1 to columns) board(2)(column).piece = "p"

    for(column <- 1 to columns) board(7)(column).piece = "P"
    board(8)(1).piece = "R"
    board(8)(2).piece = "N"
    board(8)(3).piece = "B"
    board(8)(4).piece = "Q"
    board(8)(5).piece = "K"
    board(8)(6).piece = "B"
    board(8)(7).piece = "N"
    board(8)(8).piece = "R"
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
          buf ++= board(row)(column).piece
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
