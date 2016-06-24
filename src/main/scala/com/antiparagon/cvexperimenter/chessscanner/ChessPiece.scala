package com.antiparagon.cvexperimenter.chessscanner

/**
  * Created by wmckay on 6/22/16.
  */
object ChessPiece {

  // Holds the piece symbol
  var symbol = "P"

  // Has utility functions
  def isWhite(): Boolean = {
    return symbol == symbol.toUpperCase()
  }

  def isBlack(): Boolean = {
    return symbol == symbol.toLowerCase()
  }
}
