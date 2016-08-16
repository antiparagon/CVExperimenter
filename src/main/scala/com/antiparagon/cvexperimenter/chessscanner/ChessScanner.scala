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
  var chessboardBBox: Rect = null
  var boardImage: Mat = null
  var squares: ArrayBuffer[Rect] = ArrayBuffer.empty[Rect]
  val chessboard: Chessboard = new Chessboard

  /**
    * Clears the state of ChessScanner.
    */
  def reset(): Unit = {
    fullImage = null
    chessboardBBox = null
    boardImage = null
    squares = ArrayBuffer.empty[Rect]
    chessboard.clearBoard()
  }

  /**
    * Finds a chessboard in an image.
    * @param inImg that contains a chessboard to find
    * @return the chessboard in the image or None if not found
    */
  def findChessboard(inImg: Mat): Option[Mat] = {
    reset()
    fullImage = inImg.clone()
    val bbox = ChessboardFinder.findChessboard(fullImage)
    bbox match {
      case Some(bbox) => {
        chessboardBBox = bbox
        boardImage = new Mat(inImg, bbox)
      }
      case None => {
        println("No chessboard found")
      }
    }
    if(boardImage != null)
      Some(boardImage)
    else
      None
  }

  /**
    * Finds the Rect of the squares of the chessboard image returned by findChessboard() above.
    * @return ArrayBuffer of Rect for the chessboard squares
    */
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

  /**
    * Finds the pieces on the chessboard found by findChessboard() using the squares
    * found by findSquares().
    * @return the Chessboard with all the pieces on the correct squares
    */
  def findPieces(): Option[Chessboard] = {
    None
  }

  /**
    * Returns the chess piece postion in FEN notation.
    *
    * @return Option string FEN postion of chess pieces
    */
  def getFenPosition(): Option[String] = {
    chessboard.getFenPosition()
  }

  /**
    * Draws the squares on the boardImage chessboard.
    */
  def drawSquares(): Unit = {
    if(boardImage == null) return
    val squares = chessboard.getSquares()
    squares.foreach(square => drawSquare(square))
  }

  /**
    * Draws the square on the boardImage chessboard.
    */
  def drawSquare(square: ChessSquare): Unit = {
    if(boardImage == null) return
    Imgproc.rectangle(boardImage, square.rect.tl, square.rect.br, new Scalar(0.0, 255.0, 0.0), 3)
  }

  /**
    * Draws the algebraic coordinates on the boardImage chessboard square.
    */
  def drawSquaresCoor(): Unit = {
    if(boardImage == null) return
    val squares = chessboard.getSquares()
    squares.foreach(square => drawSquareCoor(square))
  }

  /**
    * Draws the square's coordinates on the boardImage chessboard.
    * @param square to use dor coordinates.
    */
  def drawSquareCoor(square: ChessSquare): Unit = {
    if(boardImage == null) return
    val (col, row) = chessboard.translateMatrixCoor(square.row, square.column)
    val coorStr = col + row.toString
    val point = new Point(square.rect.tl.x + 5.0, square.rect.br.y - 5.0)
    Imgproc.putText(boardImage, coorStr, point, Core.FONT_HERSHEY_PLAIN, 1.2, new Scalar(0.0, 0.0, 255.0))
  }


  /**
    * Draws the squares on the fullImage chessboard.
    */
  def drawSquaresFull(): Unit = {
    if(fullImage == null) return
    if(boardImage == null) return
    if(chessboardBBox == null) return
    val squares = chessboard.getSquares()
    squares.foreach(square => {
      val offsetSquare = new Rect(square.rect.x + chessboardBBox.x, square.rect.y + chessboardBBox.y,
                                square.rect.width, square.rect.height)
      Imgproc.rectangle(fullImage, offsetSquare.tl, offsetSquare.br, new Scalar(0.0, 255.0, 0.0), 3)
    })
  }

  /**
    * Draws the algebraic coordinates on the fullImage chessboard square.
    */
  def drawSquaresCoorFull(): Unit = {
    if(fullImage == null) return
    if(boardImage == null) return
    if(chessboardBBox == null) return
    val squares = chessboard.getSquares()
    squares.foreach(square => {
      val (col, row) = chessboard.translateMatrixCoor(square.row, square.column)
      val coorStr = col + row.toString
      val offsetSquare = new Rect(square.rect.x + chessboardBBox.x, square.rect.y + chessboardBBox.y,
        square.rect.width, square.rect.height)
      val point = new Point(offsetSquare.tl.x + 5.0, offsetSquare.br.y - 5.0)
      Imgproc.putText(fullImage, coorStr, point, Core.FONT_HERSHEY_PLAIN, 1.2, new Scalar(0.0, 0.0, 255.0))
    })
  }

}
