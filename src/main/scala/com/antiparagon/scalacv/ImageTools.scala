package com.antiparagon.scalacv

import java.awt.image.BufferedImage

import org.opencv.core.{CvType, Mat}

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
    val viewImage: BufferedImage  = new BufferedImage(mat.cols(), mat.rows(), imgType)
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

}
