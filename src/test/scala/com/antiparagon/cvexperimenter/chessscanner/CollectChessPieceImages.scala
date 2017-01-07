package com.antiparagon.cvexperimenter.chessscanner

import java.nio.file.{Files, Paths}


/**
  * Created by wmckay on 1/6/17.
  */
object CollectChessPieceImages {

  val IMG_FOLDER = "ChessSquares/"

  def main(args: Array[String]): Unit = {
    createFolders()
  }

  def createFolders(): Unit = {
    val rookFolder = Paths.get(IMG_FOLDER + "Rook")
    Files.createDirectories(rookFolder)
    val knightFolder = Paths.get(IMG_FOLDER + "Knight")
    Files.createDirectories(knightFolder)
    val bishopFolder = Paths.get(IMG_FOLDER + "Bishop")
    Files.createDirectories(bishopFolder)
    val queenFolder = Paths.get(IMG_FOLDER + "Queen")
    Files.createDirectories(queenFolder)
    val kingFolder = Paths.get(IMG_FOLDER + "King")
    Files.createDirectories(kingFolder)
    val pawnFolder = Paths.get(IMG_FOLDER + "Pawn")
    Files.createDirectories(pawnFolder)
  }
}
