package com.antiparagon.cvexperimenter.chessscanner

import java.io.File

import smile.data.{AttributeDataset, NominalAttribute}
import smile.data.parser.DelimitedTextParser
import smile.neighbor.Neighbor

/**
  * Trains a KNN classifier using the FAST training data.
  *
  * Created by wmckay on 2/8/17.
  */
object TrainKnnFastClassifier {

  val TRAINING_DATA = "FastClassiferData.csv"

  def main(args: Array[String]): Unit = {
    val parser = new DelimitedTextParser()
    parser.setResponseIndex(new NominalAttribute("class"), 0)
    val usps = parser.parse("FAST Train", new File(TRAINING_DATA))
    //val x: Double[][] = usps.toArray(new double[usps.size()][]);
  }


}
