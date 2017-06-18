package scenron

import org.specs2.mutable.Specification
import StaticSparkContext._

object UnzippedToEmailPerRowDistinctSpec extends Specification {
  val sampleData = System.getProperty("user.dir") + "/sample_data"

  "UnzippedToEmailPerRowDistinct" should {
    "Take a directory to unzipped data distinct the data, and output an RDD with one email per " +
      "row (json escaped and deduplicated)" in {
      val rdd = UnzippedToEmailPerRowDistinct(sampleData, numPartitions = 10).cache()

      println("RDD.head:\n" + rdd.take(1).head)

      failure("write me")
    }
  }
}
