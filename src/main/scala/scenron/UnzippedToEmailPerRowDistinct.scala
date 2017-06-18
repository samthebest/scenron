package scenron

import org.apache.hadoop.io.compress.GzipCodec
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import spray.json.JsString

object UnzippedToEmailPerRowDistinct {
  def apply(directory: String, numPartitions: Int = 1000)(implicit sc: SparkContext): RDD[String] = {
    val duplicates =
      sc.wholeTextFiles(directory + "/edrm-enron-v2*/text*").map(p => (p._1, p._2.hashCode)).groupBy(_._2)
      .filter(_._2.size > 1).take(5).map(_._2.map(_._1).toList)

    println("DEBUG: duplicates")
    duplicates.foreach(println)

    sc.wholeTextFiles(directory + "/edrm-enron-v2*/text*").map(_._2).distinct(numPartitions)
    .map(JsString.apply).map(_.toString())
  }
}

object UnzippedToEmailPerRowDistinctApp {
  def main(args: Array[String]): Unit = {
    implicit val sc = new SparkContext(new SparkConf().setAppName("Scenron")) //.setMaster("local"))
    UnzippedToEmailPerRowDistinct("/enron/flat").saveAsTextFile("/enron/email_per_row", classOf[GzipCodec])
  }
}
