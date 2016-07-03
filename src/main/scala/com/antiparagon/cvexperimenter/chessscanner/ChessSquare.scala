package com.antiparagon.cvexperimenter.chessscanner

/**
  * Created by wmckay on 6/22/16.
  */
class ChessSquare {

  // Holds the piece symbol
  var piece = ""

  def clear() = {
    piece = ""
  }

  def isEmpty(): Boolean = {
    piece.isEmpty
  }

  // Has utility functions
  def isWhite(): Boolean = {
    if(piece.isEmpty) return false
    return piece == piece.toUpperCase()
  }

  def isBlack(): Boolean = {
    if(piece.isEmpty) return false
    return piece == piece.toLowerCase()
  }
}
