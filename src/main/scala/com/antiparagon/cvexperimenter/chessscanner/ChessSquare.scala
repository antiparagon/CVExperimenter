package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.Rect

/**
  * Created by wmckay on 6/22/16.
  */
class ChessSquare {

  // Holds the piece symbol
  var piece = ""
  // The rectangle for the square in the chessboard image
  var rect = new Rect

  def clear() = {
    piece = ""
    rect = new Rect
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
