package com.antiparagon.cvexperimenter.chessscanner

/**
  * Created by wmckay on 6/22/16.
  */
class ChessSquare {

  // Holds the piece symbol
  var symbol = ""

  // Has utility functions
  def isWhite(): Boolean = {
    if(symbol.isEmpty) return false
    return symbol == symbol.toUpperCase()
  }

  def isBlack(): Boolean = {
    if(symbol.isEmpty) return false
    return symbol == symbol.toLowerCase()
  }
}
