package com.antiparagon.cvexperimenter.chessscanner

import java.util

import org.opencv.core._
import org.opencv.imgproc.Imgproc

import scala.collection.mutable.ArrayBuffer


/**
  * Created by wmckay on 6/16/16.
  */
class ChessScanner {

  var fullImage: Mat = null
  var boardImage: Mat = null
  var squares: ArrayBuffer[Rect] = ArrayBuffer.empty[Rect]
  val chessboard: Chessboard = new Chessboard

  def findChessboard(inImg: Mat): Option[Mat] = {
    reset()
    fullImage = inImg.clone()
    boardImage = ChessboardFinder.getChessboard(fullImage).getOrElse(null)
    if(boardImage != null)
      Some(boardImage)
    else
      None
  }

  def findSquares(): ArrayBuffer[Rect] = {
    if(boardImage != null) {
      squares = ChessSquareFinder.getChessboardSquares(boardImage)
      println(s"Squares found: ${squares.size}")
      if(squares.size == 64) {
        var index = 0
        for(row <- 1 to 8) {
          for(col <- 1 to 8) {
            chessboard.getSquare(row, col).rect = squares(index)
            index += 1
          }
        }
      }
    }
    squares
  }

  def findPieces(): Option[Chessboard] = {
    None
  }

  /**
    * Returns the chess piece postions in FEN notation.
    *
    * @return Option string FEN postion of chess pieces
    */
  def getFenPosition(): Option[String] = {
    chessboard.getFenPosition()
  }

  def reset(): Unit = {
    fullImage = null
    boardImage = null
    squares = ArrayBuffer.empty[Rect]
    chessboard.clearBoard()
  }

  /**
    * Draws the squares in chessboard.
    */
  def drawSquares(): Unit = {
    if(boardImage == null) return
    val squares = chessboard.getSquares()
    squares.foreach(square => drawSquare(square))
  }

  /**
    * Draws the square on the chessboard.
    */
  def drawSquare(square: ChessSquare): Unit = {
    if(boardImage == null) return
    Imgproc.rectangle(boardImage, square.rect.tl, square.rect.br, new Scalar(0.0, 255.0, 0.0), 3)
  }

  def drawSquaresCoor(): Unit = {
    if(boardImage == null) return
    val squares = chessboard.getSquares()
    squares.foreach(square => drawSquareCoor(square))
  }

  def drawSquareCoor(square: ChessSquare): Unit = {
    if(boardImage == null) return
    val (col, row) = chessboard.translateMatrixCoor(square.row, square.column)
    val coorStr = col + row.toString
    val point = new Point(square.rect.tl.x + 5.0, square.rect.br.y - 5.0)
    Imgproc.putText(boardImage, coorStr, point, Core.FONT_HERSHEY_PLAIN, 1.2, new Scalar(0.0, 0.0, 255.0))
  }


  /**
    * Draws the squares in the squares array on the provided Mat.
    *
    * @param board
    * @param squares
    */
  def drawSquares(board: Mat, squares: ArrayBuffer[Rect]): Unit = {
    squares.foreach(square => Imgproc.rectangle(board, square.tl, square.br, new Scalar(0.0, 255.0, 0.0), 3))
  }


}
