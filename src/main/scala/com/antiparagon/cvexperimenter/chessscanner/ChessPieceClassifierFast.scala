package com.antiparagon.cvexperimenter.chessscanner

import java.io.{File, PrintStream}

import com.antiparagon.cvexperimenter.tools.ImageTools
import org.opencv.core.{Mat, MatOfDMatch, MatOfKeyPoint, Scalar}
import org.opencv.features2d.{FeatureDetector, Features2d}
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc


case class FeatureScore(avgX: Double, avgY: Double, avgResp: Double)

/**
  * Created by wmckay on 9/20/16.
  */
class ChessPieceClassifierFast {

  val features = FeatureDetector.create(FeatureDetector.FAST)
  val scores = scala.collection.mutable.Map[String, FeatureScore]()

  /**
    * Classifies the chess piece using FAST image detection features.
    *
    * @param inputImg
    * @return Some(Piece symbol) or None
    */
  def classifyPiece(inputImg: Mat, coorStr: String): Option[String] = {

    val squareImg = ImageTools.resize(inputImg, 50, 50)
    //Imgproc.cvtColor(squareImg, squareImg, Imgproc.COLOR_BGR2GRAY)
    //Imgproc.threshold(squareImg, squareImg, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU)

    val keyPointsMat = new MatOfKeyPoint()
    features.detect(squareImg, keyPointsMat)

    println(s"There were ${keyPointsMat.toArray.size} KeyPoints detected")

    val keyPoints = keyPointsMat.toArray.sortWith(_.response > _.response).take(15)

    var x = 0.0
    var y = 0.0
    var resp = 0.0
    val NL = System.lineSeparator()
    keyPoints.foreach(kp => {
      println(s"${kp}")
      x += kp.pt.x
      y += kp.pt.y
      resp += kp.response.toDouble
    })

    if(keyPoints.length >= 5) {

      val bestKeyPoints: MatOfKeyPoint = new MatOfKeyPoint(keyPoints: _*)

      Features2d.drawKeypoints(squareImg, bestKeyPoints, squareImg, new Scalar(0, 0, 255), Features2d.DRAW_RICH_KEYPOINTS)
      val imgPath = "ChessSquares/" + coorStr + ".png"
      Imgcodecs.imwrite(imgPath, squareImg)

      x = x / keyPoints.length.toDouble
      y = y / keyPoints.length.toDouble
      resp = resp / keyPoints.length.toDouble

      scores += (coorStr -> FeatureScore(x, y, resp))

      val points = keyPoints.length
      var piece = "P"

      Some(piece)
    } else {
      None
    }
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