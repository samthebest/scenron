package scenron

import org.apache.spark.rdd.RDD
import org.specs2.mutable.Specification
import StaticSparkContext.sc

object EmailStatsSpec extends Specification {
  "EmailStats.apply" should {
    // We only need one test here to check things are wired together properly. The code was used to generate the numbers
    // hence the "Regression" comment - meaning we block a regression (TODO but should really check the numbers somehow with
    // some independent reproduction).
    "Return meaningful EmailStatsSemigroup when run on the sample_enron data" in {
      val rdd: RDD[String] =
        UnzippedToEmailPerRowDistinct(UnzippedToEmailPerRowDistinctSpec.sampleData, numPartitions = 10).cache()
        .map(UnzippedToEmailPerRowDistinct.parse(_))

      // Regression
      val emailStatsSemigroup = EmailStats(rdd)

      emailStatsSemigroup.totalBodyWords must_=== 15199
      emailStatsSemigroup.numEmails must_=== 101
      EmailStatsSemigroup.topNRecipients(emailStatsSemigroup, 4) must_=== List(
        "Rick Buy" -> 3,
        "James D Steffes" -> 3,
        "Dasovich  Jeff <Jeff.Dasovich@ENRON.com>" -> 3,
        "Jeff Dasovich" -> 2
      )
    }
  }
}
