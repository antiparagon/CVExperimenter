package com.antiparagon.cvexperimenter.chessscanner

import org.opencv.core.Mat

/**
  * Created by wmckay on 4/2/17.
  */
trait ChessPieceClassifier {

  def classifyPiece(inputImg: Mat, coorStr: String): Option[String]

}
