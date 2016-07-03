package com.antiparagon.cvexperimenter

import com.antiparagon.cvexperimenter.chessscanner.Chessboard
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by wmckay on 6/26/16.
  */
class ChessboardTest extends FlatSpec with Matchers {

  "A new chessboard" should "have all squares empty" in {
    val chessboard = new Chessboard
    for(row <- 1 to chessboard.rows) {
      for(column <- 1 to chessboard.columns) {
        chessboard.board(row)(column).isBlack() should be (false)
        chessboard.board(row)(column).isWhite() should be (false)
      }
    }
  }

  "A new chessboard" should "be 9x9 (for '1' based indexing" in {
    val chessboard = new Chessboard
    chessboard.board.size should be (9)
    chessboard.board(1).size should be (9)
  }


  "A new chessboard" should "have an empty FEN position" in {
    val chessboard = new Chessboard
    chessboard.getFenPosition().get should be ("8/8/8/8/8/8/8/8")
  }

  "A chessboard with the starting position" should "have a starting FEN position" in {
    val chessboard = new Chessboard
    chessboard.setStartPosition
    chessboard.getFenPosition().get should be ("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR")
  }

  "A chessboard with the starting position" should "have a K on e1 and k on e8" in {
    val chessboard = new Chessboard
    chessboard.setStartPosition
    chessboard.getPiece("e", 8) should be ("k")
    chessboard.getPiece("e", 7) should be ("p")
    chessboard.getPiece("e", 6) should be ("")
    chessboard.getPiece("e", 5) should be ("")
    chessboard.getPiece("e", 4) should be ("")
    chessboard.getPiece("e", 3) should be ("")
    chessboard.getPiece("e", 2) should be ("P")
    chessboard.getPiece("e", 1) should be ("K")
  }

  "A chessboard with the starting position" should "have a white K on e1 and black k on e8" in {
    val chessboard = new Chessboard
    chessboard.setStartPosition
    chessboard.isWhite("e", 1) should be (true)
    chessboard.isWhite("e", 8) should be (false)

  }

}
