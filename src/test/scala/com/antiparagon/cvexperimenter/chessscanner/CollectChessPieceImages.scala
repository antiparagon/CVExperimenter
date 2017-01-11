package com.antiparagon.cvexperimenter.chessscanner

import java.io.File
import java.nio.file.{Files, Path, Paths, StandardCopyOption}


/**
  * Created by wmckay on 1/6/17.
  */
object CollectChessPieceImages {

  val IMG_FOLDER = "ChessSquares/"

  def main(args: Array[String]): Unit = {
    createFolders()
    collectStartingPositionImages()
  }


  def collectStartingPositionImages(): Unit = {
    var allImages = getListOfFiles(IMG_FOLDER)
    allImages.foreach(img => {
      if(startsWith(img.getName(), List("a1", "h1", "a8", "h8"))) {
        copyImg(img, Paths.get(IMG_FOLDER + "Rook/"))
      }
    })
    //val rooks = allImages.filter(_.getName().startsWith("a1"))
  }

  def copyImg(img: File, destination: Path, doMove: Boolean = false): Unit = {
    println(s"Moving ${img.getName()} to $destination")

    Not working it is writing over the Rook folder

    if(doMove) {
      Files.move(img.toPath, destination, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING)
    } else {
      Files.copy(img.toPath, destination, StandardCopyOption.REPLACE_EXISTING)
    }

  }

  def startsWith(filename: String, prefixes: List[String]): Boolean = {
    val prefix = prefixes.find(prefix => filename.startsWith(prefix))
    prefix match {
      case Some(filename) => return true
      case None => return false
    }
  }

  def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }

  /**
    * Helper function to create the folders the images are being collected
    * into. Creates the folders if they don't exist.
    */
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
