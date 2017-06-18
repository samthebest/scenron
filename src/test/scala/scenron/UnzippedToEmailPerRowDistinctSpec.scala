package scenron

import org.specs2.mutable.Specification
import StaticSparkContext._

object UnzippedToEmailPerRowDistinctSpec extends Specification {
  val sampleData = System.getProperty("user.dir") + "/sample_enron"

  "UnzippedToEmailPerRowDistinct" should {
    "Take a directory to unzipped data distinct the data, and output an RDD with one email per " +
      "row (json escaped and deduplicated)" in {
      val rdd = UnzippedToEmailPerRowDistinct(sampleData, numPartitions = 10).cache()

      // We manually duplicated 2 files, there are 103 files, so we expect count 101
      rdd.count() must_=== 101

      rdd.take(1).head must_=== "\"Date: Sun, 22 Sep 2002 09:42:26 -0700 (PDT)\\r\\nFrom: Dasovich, Jeff </O=ENRON/OU=NA/CN=RECIPIENTS/CN=JDASOVIC>\\r\\nSubject: Daily Call\\r\\nX-SDOC: 776751\\r\\nX-ZLID: zl-edrm-enron-v2-dasovich-j-71908.eml\\r\\n\\r\\n1-800-998-2462/5010418\\r\\n\\r\\n***********\\r\\nEDRM Enron Email Data Set has been produced in EML, PST and NSF format by ZL Technologies, Inc. This Data Set is licensed under a Creative Commons Attribution 3.0 United States License <http://creativecommons.org/licenses/by/3.0/us/> . To provide attribution, please cite to \\\"ZL Technologies, Inc. (http://www.zlti.com).\\\"\\r\\n***********\\r\\n\""

      rdd.getNumPartitions must_=== 10
    }
  }
}
