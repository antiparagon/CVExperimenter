package com.antiparagon.cvexperimenter.chessscanner

import java.io.File

import smile.data.{AttributeDataset, NominalAttribute, NumericAttribute, StringAttribute}
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
    parser.setDelimiter(",")
    parser.setColumnNames(true)
    val aAvgX = new NumericAttribute("AvgX")
    val aAvgY = new NumericAttribute("AvgY")
    val aAvgResp = new NumericAttribute("AvgResp")
    val aCoord = new StringAttribute("Coord")
    val aSymbol = new NominalAttribute("Symbol")
    val aImage = new StringAttribute("Image")

    //val attributes = Array(new Attribute)
    parser.setResponseIndex(aSymbol, 5)
    val attData = parser.parse("FAST Train", new File(TRAINING_DATA))
    //val x: Double[][] = usps.toArray(new double[usps.size()][]);

    println(s"Num Attr: ${attData.attributes.size}")

    for(att <- attData.attributes()) {
      println(s"Attr: ${att.getName}")
    }

  }


}
