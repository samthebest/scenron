package scenron

import java.io.File

import org.specs2.mutable.Specification
import StaticSparkContext.sc

object UnzippedToEmailPerRowDistinctAltSpec extends Specification {
  val tmpDir = System.getProperty("user.dir") + "/tmp_dir"

  "UnzippedToEmailPerRowDistinctAlt" should {
    "Take a directory to unzipped data distinct the data, and output an RDD with one email per " +
      "row (json escaped and deduplicated)" in {
      val rdd = UnzippedToEmailPerRowDistinctAlt(
        UnzippedToEmailPerRowDistinctSpec.sampleData,
        numPartitions = 10,
        tmpDir = tmpDir
      ).cache()

      // We manually duplicated 2 files, there are 103 files, so we expect count 101
      rdd.count() must_=== 101

      new File(tmpDir).listFiles().foreach(_.delete())
      new File(tmpDir).delete()

      rdd.getNumPartitions must_=== 10
    }
  }
}
