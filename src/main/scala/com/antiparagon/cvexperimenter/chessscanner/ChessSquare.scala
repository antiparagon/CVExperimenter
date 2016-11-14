package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.Rect

/**
  * Class to hold the piece symbol and image rectangle for
  * a chess square.
  *
  * Created by wmckay on 6/22/16.
  */
class ChessSquare(val row: Int, val column: Int) {

  // Holds the piece symbol
  var piece = ""
  // The rectangle for the square in the chessboard image
  var rect = new Rect

  /**
    * Clears the piece and resets the Rect for the ChessSquare.
    */
  def clear() = {
    piece = ""
    rect = new Rect
  }

  /**
    * Determines if there is a piece on the square.
    * @return true if there is a piece on the square
    */
  def isEmpty(): Boolean = {
    piece.isEmpty
  }

  /**
    * Determines if the piece on the square is white.
    * @return true if the piece is white, false otherwise
    */
  def isWhite(): Boolean = {
    if(piece.isEmpty) return false
    return piece == piece.toUpperCase()
  }

  /**
    * Determines of the piece on the square is black.
    * @return true if the piece is black, false otherwise
    */
  def isBlack(): Boolean = {
    if(piece.isEmpty) return false
    return piece == piece.toLowerCase()
  }

  override def clone(): ChessSquare = {
    val square = new ChessSquare(row, column)
    square.rect = rect.clone()
    square
  }
}
