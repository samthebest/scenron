package scenron

import org.specs2.mutable.Specification

object EmailStatsMonoidSpec extends Specification {
  "EmailStatsMonoid.apply" should {
    "Given an Email produces correct EmailStatsMonoid" in {
      EmailStatsMonoid(Email(List("fred", "alice"), List("bob", "dave"), "hello world")) must_=== EmailStatsMonoid(
        toCounts = Map("fred" -> 1, "alice" -> 1),
        ccCounts = Map("bob" -> 1, "dave" -> 1),
        totalBodyWords = 2,
        numEmails = 1
      )
    }
  }
}
