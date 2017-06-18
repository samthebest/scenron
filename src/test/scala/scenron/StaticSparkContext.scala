package scenron

import org.apache.spark.{SparkConf, SparkContext}

object StaticSparkContext {
  implicit val sc = new SparkContext(new SparkConf().setAppName("Scenron").setMaster("local"))
}
