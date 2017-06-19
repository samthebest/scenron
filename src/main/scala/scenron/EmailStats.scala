package scenron

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import EmailStatsSemigroup.semigroupSyntax._

object EmailStats {
  def apply(rdd: RDD[String]): EmailStatsSemigroup =
    rdd.map(ParseEmail(_)).map(EmailStatsSemigroup(_)).reduce(_ |+| _)
}

object EmailStatsApp {
  def main(args: Array[String]): Unit = {
    implicit val sc = new SparkContext(new SparkConf().setAppName("Scenron"))
    val emailStatsSemigroup = EmailStats.apply(UnzippedToEmailPerRowDistinct.read())
    println("Total Number of Emails: " + emailStatsSemigroup.numEmails)
    println("Average Body Length: " + (emailStatsSemigroup.totalBodyWords.toDouble / emailStatsSemigroup.numEmails))
    println("\n\nTop 100 Recipients:")
    EmailStatsSemigroup.topNRecipients(emailStatsSemigroup, 100).foreach {
      case (name, count) => println(name + "\t" + count)
    }
  }
}
