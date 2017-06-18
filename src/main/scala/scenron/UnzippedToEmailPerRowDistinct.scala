package scenron

import org.apache.hadoop.io.compress.GzipCodec
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import spray.json._
import spray.json.DefaultJsonProtocol
import spray.json.DefaultJsonProtocol._

object UnzippedToEmailPerRowDistinct {
  val emailPerRowDir = "file:/enron/email_per_row"

  def apply(directory: String, numPartitions: Int = 1000)(implicit sc: SparkContext): RDD[String] =
    sc.wholeTextFiles(directory + "/edrm-enron-v2*/text*")
    .map(_._2).distinct(numPartitions)
    .map(JsString.apply).map(_.toString())

  // TODO Should replace "\r\n" with "\n" in `apply`
  def read(directory: String = emailPerRowDir)(implicit sc: SparkContext): RDD[String] =
    sc.textFile(directory).map(_.parseJson.convertTo[String].replaceAllLiterally("\r\n", "\n"))
}

import UnzippedToEmailPerRowDistinct._

object UnzippedToEmailPerRowDistinctApp {
  def main(args: Array[String]): Unit = {
    implicit val sc = new SparkContext(new SparkConf().setAppName("Scenron"))
    UnzippedToEmailPerRowDistinct("file:/enron/flat").saveAsTextFile(emailPerRowDir, classOf[GzipCodec])
  }
}
