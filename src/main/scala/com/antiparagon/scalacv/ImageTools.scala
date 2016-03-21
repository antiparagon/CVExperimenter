package com.antiparagon.scalacv

import java.awt.image.BufferedImage

import org.opencv.core.{CvType, Mat}
import org.opencv.videoio.{Videoio, VideoCapture}

import scalafx.embed.swing.SwingFXUtils
import scalafx.scene.image.{PixelFormat, Image, WritableImage}

/**
  * Created by wmckay on 3/17/16.
  */
object ImageTools {

  def converCVtoFX(mat:Mat) : WritableImage = {
    var imgType = BufferedImage.TYPE_BYTE_GRAY
    if ( mat.channels() > 1 ) {
      imgType = BufferedImage.TYPE_3BYTE_BGR
    }

    val arraySize = mat.channels() * mat.cols() * mat.rows()
    val b = new Array[Byte](arraySize)
    mat.get(0, 0, b)
    val viewImage  = new BufferedImage(mat.cols(), mat.rows(), imgType)
    viewImage.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), b)
    SwingFXUtils.toFXImage(viewImage, null)
  }

  def converFXtoCV(img:Image) : Mat = {
    val width = img.width.value.toInt
    val height = img.height.value.toInt
    val buffer = new Array[Byte](width * height * 4)

    val reader = img.getPixelReader
    val format = PixelFormat.getByteBgraInstance
    reader.getPixels(0, 0, width, height, format, buffer, 0, width * 4)

    val mat = new Mat(height, width, CvType.CV_8UC4)
    mat.put(0, 0, buffer)
    return mat
  }

  def getVideoCaptureProperties(capture: VideoCapture): Map[String, Double] = {
    val prop = Map[String, Double](
      "Position"-> capture.get(Videoio.CAP_PROP_POS_MSEC),
      "Frames"-> capture.get(Videoio.CAP_PROP_POS_FRAMES),
      "AviRatio"-> capture.get(Videoio.CAP_PROP_POS_AVI_RATIO),
      "Width" -> capture.get(Videoio.CAP_PROP_FRAME_WIDTH), // 1280
      "Height" -> capture.get(Videoio.CAP_PROP_FRAME_HEIGHT), // 720
      "FPS"-> capture.get(Videoio.CAP_PROP_FPS),
      "FourCC" -> capture.get(Videoio.CAP_PROP_FOURCC),
      "FrameCount" -> capture.get(Videoio.CAP_PROP_FRAME_COUNT),
      "Format" -> capture.get(Videoio.CAP_PROP_FORMAT),
      "Brightness" -> capture.get(Videoio.CAP_PROP_BRIGHTNESS),
      "Contrast" -> capture.get(Videoio.CAP_PROP_CONTRAST),
      "Saturation" -> capture.get(Videoio.CAP_PROP_SATURATION),
      "Hue" -> capture.get(Videoio.CAP_PROP_HUE),
      "Gain" -> capture.get(Videoio.CAP_PROP_GAIN),
      "Exposure" -> capture.get(Videoio.CAP_PROP_EXPOSURE),
      "ConvertRGB" -> capture.get(Videoio.CAP_PROP_CONVERT_RGB),
      //"WhiteBalance" -> capture.get(Videoio.CAP_PROP_W),
      "Rectification" -> capture.get(Videoio.CAP_PROP_RECTIFICATION)
    )

    return prop
  }

  def getMatProperties(mat: Mat): Map[String, Int] = {
    val prop = Map[String, Int](
      "Channels" -> mat.channels(),
      "Depth" -> mat.depth()
    )
    return prop
  }

  def depthToString(depth: Int): String = {
    depth match {
      case 0 => "CV_8U"
      case 1 => "CV_8S"
      case 2 => "CV_16U"
      case 3 => "CV_16S"
      case 4 => "CV_32S"
      case 5 => "CV_32F"
      case 6 => "CV_64F"
      case 7 => "CV_USRTYPE1"
      case _ => "UNKNOWN"
    }
  }
}
