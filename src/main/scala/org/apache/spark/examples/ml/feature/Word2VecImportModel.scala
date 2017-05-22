package org.apache.spark.examples.ml.feature

import org.apache.spark.{SparkConf, SparkContext}
import java.io.DataInputStream
import java.util.zip.GZIPInputStream

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class Word2VecImportModel {

}

/**
  * Created by marius on 5/17/17.
  */
object Word2VecImportModel {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Test load Google Word2Vec")
                              .setMaster("local[*]")
    val sc = new SparkContext(conf)

    val path = "file:///media/data/repo/deeplearning4j/GoogleNews-vectors-negative300.bin.gz"
    val word = "idea"
    val gModel = loadGoogleModel(sc, path)
    println(gModel.transform(word).toArray.mkString(", "))

    sc.stop()
  }

  /**
    * Load Google word2vec model
    *
    * @see [[https://code.google.com/p/word2vec/]]
    * @param sc SparkContext object
    * @param file Google word2vec model file
    * @return a Word2VecModel
    */
  def loadGoogleModel(sc: SparkContext, file: String): Word2VecModel = {

    // ASCII values for common delimiter characters.
    val SPACE = 32
    val LF = 10

    // Read a string from the data input stream.
    def readToken(dis: DataInputStream, delimiters: Set[Int] = Set(SPACE, LF)): String = {
      val bytes = new ArrayBuffer[Byte]()
      val sb = new StringBuilder()
      var byte = dis.readByte()
      while (!delimiters.contains(byte)) {
        bytes.append(byte)
        byte = dis.readByte()
      }
      sb.append(new String(bytes.toArray[Byte])).toString()
    }

    // Read a float from the data input stream.
    def readFloat(dis: DataInputStream): Float = {
      java.lang.Float.intBitsToFloat(Integer.reverseBytes(dis.readInt()))
    }

    // Read a model file.
    val pds = sc.binaryFiles(file).first()._2
    val dis = new DataInputStream(new GZIPInputStream(pds.open()))

    val vocab = new mutable.HashMap[String, Array[Float]]()

    // Read header info.
    val numWords = readToken(dis).toInt
    val vecSize = readToken(dis).toInt

    val vector = new Array[Float](vecSize)
    for (_ <- 0 until numWords) {
      // Read the word.
      val word = readToken(dis)
      // Read the vector.
      for (i <- 0 until vecSize) vector(i) = readFloat(dis)
      // Store the normalized vector representation.
      val normFactor = math.sqrt(vector.foldLeft(0.0) { (sum, x) => sum + (x * x) }).toFloat
      vocab.put(word, vector.map(_ / normFactor))

      dis.read()
    }

    dis.close()
    new Word2VecModel(vocab.toMap)
  }

}
