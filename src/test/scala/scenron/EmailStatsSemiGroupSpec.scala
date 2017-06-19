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

  "EmailStatsSemigroup.topNRecipients" should {
    "Return the top 2 recipients where CC counts are halved (in descending order)" in {
      EmailStatsSemigroup.topNRecipients(EmailStatsSemigroup(
        toCounts = Map("fred" -> 4, "alice" -> 1),
        ccCounts = Map("bob" -> 6, "george" -> 1, "dave" -> 1, "fred" -> 5),
        totalBodyWords = 14,
        numEmails = 4
      ), 2) must_=== List(
        "fred" -> 6,
        "bob" -> 3
      )
    }

    "Return the top 3 recipients where CC counts are halved (in descending order)" in {
      EmailStatsSemigroup.topNRecipients(EmailStatsSemigroup(
        toCounts = Map("fred" -> 4, "alice" -> 10, "sarah" -> 20),
        ccCounts = Map("bob" -> 30, "george" -> 1, "dave" -> 1, "fred" -> 5, "alice" -> 3),
        totalBodyWords = 14,
        numEmails = 4
      ), 3) must_=== List(
        "sarah" -> 20,
        "bob" -> 15,
        "alice" -> 11
      )
    }
  }
}
