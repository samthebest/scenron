package scenron

import java.io.{PrintWriter, File}

import org.apache.hadoop.io.compress.GzipCodec
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import scenron.Paths._
import spray.json.JsString

import scala.io.Source

object UnzippedToEmailPerRowDistinctAlt {
  def apply(directory: String, numPartitions: Int = 1000, tmpDir: String = "/enron/email_per_row_non_distinct")
           (implicit sc: SparkContext): RDD[String] = {
    val inputDir = new File(directory)

    val tmpDirFile = new File(tmpDir)

    if (tmpDirFile.exists() && tmpDirFile.isDirectory)
      println("INFO: Skipping email per row processing, will distinct")
    else {
      tmpDirFile.mkdir()

      inputDir.listFiles().foreach(userDir => {
        println("INFO: Processing: " + userDir.getName)
        val pw = new PrintWriter(new File(tmpDir + "/" + userDir.getName))
        try
          userDir.listFiles().foreach { textDir =>
            println("INFO: Processing: " + textDir.getName)
            textDir.listFiles().foreach { emailFile =>
              pw.append(JsString(Source.fromFile(emailFile).mkString).toString() + "\n")
            }
          }
        finally pw.close()
      })
    }

    println("INFO: Distincting")
    sc.textFile(tmpDir).distinct(numPartitions)
  }
}

object UnzippedToEmailPerRowDistinctAltApp {
  def main(args: Array[String]): Unit = {
    implicit val sc = new SparkContext(new SparkConf().setAppName("Scenron"))
    UnzippedToEmailPerRowDistinctAlt("file:" + unzipped).saveAsTextFile(emailPerRowDirAlt, classOf[GzipCodec])
  }
}
