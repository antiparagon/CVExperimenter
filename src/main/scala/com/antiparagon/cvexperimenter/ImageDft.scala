package com.antiparagon.cvexperimenter

import org.opencv.core.{Core, Mat, Scalar}


/**
  * Created by wmckay on 5/24/16.
  */
object ImageDft {

  def optimizeImageDim(image: Mat): Mat =  {
    // init
    val padded = new Mat()
    // get the optimal rows size for dft
    val addPixelRows = Core.getOptimalDFTSize(image.rows());
    // get the optimal cols size for dft
    val addPixelCols = Core.getOptimalDFTSize(image.cols());
    // apply the optimal cols and rows size to the image
    Core.copyMakeBorder(image, padded, 0, addPixelRows - image.rows(), 0, addPixelCols - image.cols(), Core.BORDER_CONSTANT, Scalar.all(0));

    return padded;
  }

}
