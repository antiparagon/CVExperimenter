package com.antiparagon.cvexperimenter.chessscanner

import com.typesafe.scalalogging.Logger
import org.opencv.core._
import org.opencv.imgproc.Imgproc
import org.slf4j.LoggerFactory


/**
  * Created by wmckay on 6/16/16.
  */
class ChessScanner {

  val log = Logger(LoggerFactory.getLogger("ChessScanner"))
  var fullImage: Mat = null
  var chessboardBBox: Rect = null
  var boardImage: Mat = null
  var squares: Array[Rect] = Array.empty[Rect]
  val chessboard: Chessboard = new Chessboard

  /**
    * Clears the state of ChessScanner.
    */
  def reset(): Unit = {
    log.debug("Reseting ChessScanner")
    fullImage = null
    chessboardBBox = null
    boardImage = null
    squares = Array.empty[Rect]
    chessboard.clearBoard()
  }

  /**
    * Finds a chessboard in an image.
    * @param inImg that contains a chessboard to find
    * @return the chessboard in the image or None if not found
    */
  def findChessboard(inImg: Mat): Option[Mat] = {
    reset()
    if(inImg == null || inImg.empty()) {
      return None
    }
    fullImage = inImg.clone()
    val bbox = ChessboardFinder.findChessboard(fullImage)
    bbox match {
      case Some(bbox) => {
        chessboardBBox = bbox
        log.debug(bbox.toString)
        boardImage = new Mat(inImg, bbox)
      }
      case None => {
        reset()
        println("No chessboard found")
      }
    }
    Option(boardImage)
  }

  /**
    * Finds the Rect of the squares of the chessboard image returned by findChessboard() above.
    * @return Array of Rect for the chessboard squares
    */
  def findSquares(): Array[Rect] = {
    if(boardImage != null) {
      squares = ChessSquareFinder.getChessboardSquares(boardImage)
      log.debug(s"Squares found: ${squares.size}")
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
  def findPieces(): Int = {
    if(boardImage != null) {
      if(squares.size == 64) {
        val piecesFound = ChessPieceFinder.findChessPieces(chessboard, boardImage)
        return piecesFound
      }
    }
    return 0
  }

  /**
    * Returns the chess piece postion in FEN notation.
    *
    * @return string FEN postion of chess pieces
    */
  def getFenPosition(): String = {
    run()
    chessboard.getFenPosition()
  }

  /**
    * Draws the squares on the boardImage chessboard.
    */
  def drawSquares(): Unit = {
    if(boardImage == null) return
    chessboard.getSquares().foreach(square => drawSquare(square))
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
    chessboard.getSquares().foreach(square => drawSquareCoor(square))
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
    * Draws the symbol for the piece on the boardImage chessboard square.
    */
  def drawPieceSymbols(): Unit = {
    if(boardImage == null) return
    chessboard.getSquares().filter(_.piece != "").foreach(square => {
      println(s"Found ${square.piece}")
      val point = new Point(square.rect.tl.x + 5.0, square.rect.br.y - 5.0)
      Imgproc.putText(boardImage, square.piece, point, Core.FONT_HERSHEY_PLAIN, 1.2, new Scalar(0.0, 255.0, 0.0), 3)
    })
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
      val offsetSquare = new Rect(square.rect.x + chessboardBBox.x, square.rect.y + chessboardBBox.y, square.rect.width, square.rect.height)
      val point = new Point(offsetSquare.tl.x + 5.0, offsetSquare.br.y - 5.0)
      Imgproc.putText(fullImage, coorStr, point, Core.FONT_HERSHEY_PLAIN, 1.2, new Scalar(0.0, 0.0, 255.0))
    })
  }

  /**
    * Draws the symbol for the piece on the fullImage chessboard square.
    */
  def drawPieceSymbolsFull(): Unit = {
    if(fullImage == null) return
    if(boardImage == null) return
    if(chessboardBBox == null) return
    chessboard.getSquares().filter(_.piece != "").foreach(square => {
      val offsetSquare = new Rect(square.rect.x + chessboardBBox.x, square.rect.y + chessboardBBox.y, square.rect.width, square.rect.height)
      val point = new Point(offsetSquare.tl.x + 12.0, offsetSquare.br.y - 12.0)
      Imgproc.putText(fullImage, square.piece, point, Core.FONT_HERSHEY_COMPLEX, 1.0, new Scalar(255.0, 200.0, 0.0), 2)
    })
  }


  def run() {
    import scala.reflect._
    import org.opencv.features2d.{DescriptorExtractor, DescriptorMatcher, FeatureDetector, Features2d}
    import org.opencv.imgcodecs.Imgcodecs
    import org.opencv.imgproc.Imgproc

    println(s"\nRunning ${classTag[this.type].toString.replace("$", "")}")

    // Detects keypoints and extracts descriptors in a given image of type Mat.
    def detectAndExtract(mat: Mat) = {
      // A special container class for KeyPoint.
      val keyPoints = new MatOfKeyPoint
      // We're using the ORB detector.
      val detector = FeatureDetector.create(FeatureDetector.ORB)
      detector.detect(mat, keyPoints)

      println(s"There were ${keyPoints.toArray.size} KeyPoints detected")

      // Let's just use the best KeyPoints.
      val sorted = keyPoints.toArray.sortBy(_.response).reverse.take(50)
      // There isn't a constructor that takes Array[KeyPoint], so we unpack
      // the array and use the constructor that can take any number of
      // arguments.
      val bestKeyPoints: MatOfKeyPoint = new MatOfKeyPoint(sorted: _*)

      // We're using the ORB descriptor.
      val extractor = DescriptorExtractor.create(DescriptorExtractor.ORB)
      val descriptors = new Mat
      extractor.compute(mat, bestKeyPoints, descriptors)

      println(s"${descriptors.rows} descriptors were extracted, each with dimension ${descriptors.cols}")

      (bestKeyPoints, descriptors)
    }

    // Load the images from the |resources| directory.
    val leftImage = Imgcodecs.imread("img1.png")
    val rightImage = Imgcodecs.imread("img2.png")

    // Detect KeyPoints and extract descriptors.
    val (leftKeyPoints, leftDescriptors) = detectAndExtract(leftImage)
    val (rightKeyPoints, rightDescriptors) = detectAndExtract(rightImage)

    // Match the descriptors.
    val matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE)
    // A special container class for DMatch.
    val dmatches = new MatOfDMatch
    // The backticks are because "match" is a keyword in Scala.
    matcher.`match`(leftDescriptors, rightDescriptors, dmatches)

    // Visualize the matches and save the visualization.
    val correspondenceImage = new Mat
    Features2d.drawMatches(leftImage, leftKeyPoints, rightImage, rightKeyPoints, dmatches, correspondenceImage)
    val filename = "scalaCorrespondences.png"
    println(s"Writing ${filename}")
    assert(Imgcodecs.imwrite(filename, correspondenceImage))
  }
}
