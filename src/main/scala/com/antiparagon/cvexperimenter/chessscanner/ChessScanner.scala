package com.antiparagon.cvexperimenter.chessscanner

import java.util

import org.opencv.core._
import org.opencv.imgproc.Imgproc


/**
  * Created by wmckay on 6/16/16.
  */
object ChessScanner {


  def findChessboard(inImg: Mat): Option[Mat] = {
    return ChessboardFinder.getChessboard(inImg)
  }

  def findSquares(inImg: Mat): Option[Mat] = {
    return ChessSquareFinder.getChessboardSquares(inImg)
  }

  def findPieces(inImg: Mat): Option[Mat] = {
    return ChessPieceFinder.getChessPieces(inImg)
  }

  /**
    * Returns the chess piece postions in FEN notation for the 2D chessboard in an image.
    *
    * @param inImg to find position in
    * @return Option string FEN postion of chess pieces
    */
  def getFenPosition(inImg: Mat): Option[String] = {

    if(inImg == null) {
      return None
    }

    val boardImg = ChessboardFinder.getChessboard(inImg)

    // Unable to find a rectangle that has a chessboard
    if(boardImg.isEmpty) {
      return None
    }

    ChessPositionFinder.getFenPosition(boardImg.get)
  }

}
