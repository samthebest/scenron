package scenron

import org.apache.hadoop.io.compress.GzipCodec
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import spray.json._
import spray.json.DefaultJsonProtocol._
import Paths._

object UnzippedToEmailPerRowDistinct {
  def apply(directory: String, numPartitions: Int = 1000)(implicit sc: SparkContext): RDD[String] =
    sc.wholeTextFiles(directory + filePattern)
    .map(_._2).distinct(numPartitions)
    .map(JsString.apply).map(_.toString())

  def read(directory: String = hdfsEmailPerRowDir)(implicit sc: SparkContext): RDD[String] =
    sc.textFile("hdfs:" + directory).map(parse)

  def parse(line: String): String = line.parseJson.convertTo[String].replaceAllLiterally("\r\n", "\n")
}

object UnzippedToEmailPerRowDistinctApp {
  def main(args: Array[String]): Unit = {
    implicit val sc = new SparkContext(new SparkConf().setAppName("Scenron"))
    UnzippedToEmailPerRowDistinct("file:" + unzipped).saveAsTextFile(emailPerRowDir, classOf[GzipCodec])
  }
}
