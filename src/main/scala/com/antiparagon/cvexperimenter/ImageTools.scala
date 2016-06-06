package com.antiparagon.cvexperimenter

import java.awt.image.{BufferedImage}
import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import javax.imageio.ImageIO

import org.opencv.core._
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.videoio.{VideoCapture, Videoio}

import scalafx.embed.swing.SwingFXUtils
import scalafx.scene.image.{Image, WritableImage}

/**
  * Created by wmckay on 3/17/16.
  */
object ImageTools {

  def convertCVtoFX_Old(mat:Mat) : WritableImage = {
    var imgType = BufferedImage.TYPE_BYTE_GRAY
    if ( mat.channels > 1 ) {
      imgType = BufferedImage.TYPE_3BYTE_BGR
    }

    val arraySize = mat.channels * mat.cols * mat.rows
    val b = new Array[Byte](arraySize)
    mat.get(0, 0, b)
    val viewImage  = new BufferedImage(mat.cols, mat.rows, imgType)
    viewImage.getRaster.setDataElements(0, 0, mat.cols, mat.rows, b)
    SwingFXUtils.toFXImage(viewImage, null)
  }

  def convertCVtoFX(frame: Mat): WritableImage = {
    val buffer = new MatOfByte
    Imgcodecs.imencode(".png", frame, buffer)
    val bImg = ImageIO.read(new ByteArrayInputStream(buffer.toArray))
    SwingFXUtils.toFXImage(bImg, null)
  }

  def convertFXtoCV(img: Image): Mat = {
    val bImage = SwingFXUtils.fromFXImage(img, null)
    val s = new ByteArrayOutputStream()
    ImageIO.write(bImage, "png", s);
    val res  = s.toByteArray()

    val rawData  =  new Mat(res.length, 1, CvType.CV_8UC1)
    rawData.put(0, 0, res)
    val mat = Imgcodecs.imdecode(rawData, Imgcodecs.CV_LOAD_IMAGE_COLOR)
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
      "Channels" -> mat.channels,
      "Depth" -> mat.depth
    )
    return prop
  }

  def outputMatProperties(mat: Mat): Unit = {
    val prop = getMatProperties(mat)
    println(s"Channels = ${prop("Channels")}")
    println(s"Depth = ${depthToString(prop("Depth"))}")
    println(s"Size = ${mat.size}")
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

  def translate(image: Mat, x: Double, y: Double): Mat = {
    val trans = new Mat(2, 3, CvType.CV_64F)
    trans.put(0, 0, 1.0)
    trans.put(0, 1, 0.0)
    trans.put(0, 2, x)
    trans.put(1, 0, 0.0)
    trans.put(1, 1, 1.0)
    trans.put(1, 2, y)
    val shifted = image.clone
    Imgproc.warpAffine(image, shifted, trans, shifted.size)
    return shifted
  }


  def rotate(image: Mat, angle: Double, center: Point, scale: Double = 1.0): Mat = {
    val rot = Imgproc.getRotationMatrix2D(center, angle, scale)
    val rotated = new Mat
    Imgproc.warpAffine(image, rotated, rot, rotated.size)
    return rotated
  }





//  def resize(image, width=None, height=None, inter=cv2.INTER_AREA):
//  # initialize the dimensions of the image to be resized and
//  # grab the image size
//    dim = None
//  (h, w) = image.shape[:2]
//
//  # if both the width and height are None, then return the
//  # original image
//  if width is None and height is None:
//  return image
//
//  # check to see if the width is None
//  if width is None:
//  # calculate the ratio of the height and construct the
//  # dimensions
//  r = height / float(h)
//  dim = (int(w * r), height)
//
//  # otherwise, the height is None
//  else:
//  # calculate the ratio of the width and construct the
//  # dimensions
//  r = width / float(w)
//  dim = (width, int(h * r))
//
//  # resize the image
//  resized = cv2.resize(image, dim, interpolation=inter)
//
//  # return the resized image
//  return resized
  def resize(image: Mat, width: Int, height: Int, inter: Int = Imgproc.INTER_AREA): Mat = {
    val resized = new Mat
    val size = new Size(width, height)
    Imgproc.resize(image, resized, size, 0.0, 0.0, inter)
    return resized
  }

  def resize(image: Mat, ratio: Double): Mat = {
    val resized = new Mat
    Imgproc.resize(image, resized, new Size(0,0), ratio, ratio, Imgproc.INTER_AREA)
    return resized
  }


//  def skeletonize(image, size, structuring=cv2.MORPH_RECT):
//  # determine the area (i.e. total number of pixels in the image),
//  # initialize the output skeletonized image, and construct the
//  # morphological structuring element
//  area = image.shape[0] * image.shape[1]
//  skeleton = np.zeros(image.shape, dtype="uint8")
//  elem = cv2.getStructuringElement(structuring, size)
//
//  # keep looping until the erosions remove all pixels from the
//  # image
//  while True:
//  # erode and dilate the image using the structuring element
//  eroded = cv2.erode(image, elem)
//  temp = cv2.dilate(eroded, elem)
//
//  # subtract the temporary image from the original, eroded
//  # image, then take the bitwise 'or' between the skeleton
//  # and the temporary image
//    temp = cv2.subtract(image, temp)
//  skeleton = cv2.bitwise_or(skeleton, temp)
//  image = eroded.copy()
//
//  # if there are no more 'white' pixels in the image, then
//  # break from the loop
//  if area == area - cv2.countNonZero(image):
//    break
//
//    # return the skeletonized image
//  return skeleton
  def skeletonize(image: Mat, size: Size, structuring: Int = Imgproc.MORPH_RECT): Mat = {
    val area = image.width * image.height
    val skeleton = Mat.zeros(image.size, image.`type`())
    val elem = Imgproc.getStructuringElement(structuring, size)
    var img = image.clone
    while(true) {
      val eroded = new Mat(img.size, img.`type`())
      Imgproc.erode(img, eroded, elem)
      val temp = new Mat(img.size, img.`type`())
      Imgproc.dilate(eroded, temp, elem)
      Core.subtract(img, temp, temp)
      Core.bitwise_or(skeleton, temp, skeleton)
      img = eroded.clone
      if(area == (area - Core.countNonZero(img))) {
        return skeleton
      }
    }
    return img
  }


//  def auto_canny(image, sigma=0.33):
//  # compute the median of the single channel pixel intensities
//  v = np.median(image)
//
//  # apply automatic Canny edge detection using the computed median
//  lower = int(max(0, (1.0 - sigma) * v))
//  upper = int(min(255, (1.0 + sigma) * v))
//  edged = cv2.Canny(image, lower, upper)
//
//  # return the edged image
//  return edged
  def autoCanny(image: Mat, sigma: Double = 0.33): Mat = {
    null
  }

}
