package scenron

import org.specs2.mutable.Specification

object EmailStatsSemigroupSpec extends Specification {
  val example1 = EmailStatsSemigroup(
    toCounts = Map("fred" -> 1, "alice" -> 1),
    ccCounts = Map("bob" -> 1, "dave" -> 1),
    totalBodyWords = 4,
    numEmails = 1
  )

  val example2 = EmailStatsSemigroup(
    toCounts = Map("fred" -> 3),
    ccCounts = Map("bob" -> 5, "george" -> 1),
    totalBodyWords = 10,
    numEmails = 3
  )

  "EmailStatsSemigroup.apply" should {
    "Given an Email produces correct EmailStatsMonoid (ignoring any external email addresses)" in {
      EmailStatsSemigroup(Email(List("fred", "alice"), List("bob", "dave"), "\n\nhello  world\nFoo\t  bar    ")) must_===
        example1
    }
  }

  "EmailStatsSemigroup.append" should {
    "Correctly append" in {
      import EmailStatsSemigroup.semigroupSyntax._

      example1 |+| example2 must_=== EmailStatsSemigroup(
        toCounts = Map("fred" -> 4, "alice" -> 1),
        ccCounts = Map("bob" -> 6, "george" -> 1, "dave" -> 1),
        totalBodyWords = 14,
        numEmails = 4
      )
    }
  }
}
