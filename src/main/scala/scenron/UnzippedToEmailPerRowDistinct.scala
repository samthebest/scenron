package scenron

import org.apache.hadoop.io.compress.GzipCodec
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import spray.json.JsString
import spray.json.DefaultJsonProtocol

object UnzippedToEmailPerRowDistinct {
  def apply(directory: String, numPartitions: Int = 1000)(implicit sc: SparkContext): RDD[String] =
    sc.wholeTextFiles(directory + "/edrm-enron-v2*/text*")
    .map(_._2).distinct(numPartitions)
    .map(JsString.apply).map(_.toString())
}

object UnzippedToEmailPerRowDistinctApp {
  def main(args: Array[String]): Unit = {
    implicit val sc = new SparkContext(new SparkConf().setAppName("Scenron"))
    UnzippedToEmailPerRowDistinct("file:/enron/flat").saveAsTextFile("file:/enron/email_per_row", classOf[GzipCodec])
  }
}
