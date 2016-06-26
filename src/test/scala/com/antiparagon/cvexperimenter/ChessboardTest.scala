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

}
