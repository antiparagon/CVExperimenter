package com.antiparagon.cvexperimenter

import com.antiparagon.cvexperimenter.chessscanner.ChessScanner

import collection.mutable.Stack
import org.scalatest._

/**
  * Created by wmckay on 6/10/16.
  */
class ChessScannerTest extends FlatSpec with Matchers {

  "ChessScanner" should "return None when given an empty image" in {
    val chessScanner = new ChessScanner
    val pos = chessScanner.getFenPosition()
    pos should be (None)
  }

}
