package scenron

import org.specs2.mutable.Specification
import SampleEmails._

object ParseEmailSpec extends Specification {
  val expectedCcWithReply = List("Karen Tamlyn", "Dale Surbey", "Steven Leppard",
    "Melanie Doyle", "Tani Nath", "Vince J Kaminski", "Lucy Page")
  val expectedBodyWithReply =
    """I'm entirely OK with this....
      |
      |Matt""".stripMargin

  "ParseEmail.extractEmailList" should {
    "Extract empty \"Cc\" correctly" in {
      ParseEmail.extractEmailList("Cc", withEmptyCc) must_=== Nil
    }

    "Extract singleton \"To\" correctly" in {
      ParseEmail.extractEmailList("To", withForward) must_=== List("Shirley Crenshaw")
    }

    "Extract two line \"Cc\" correctly" in {
      ParseEmail.extractEmailList("Cc", withReply) must_=== expectedCcWithReply
    }

    "Extract three line \"To\" correctly" in {
      ParseEmail.extractEmailList("To", withThreeLineTo) must_=== List("Karen Tamlyn", "Dale Surbey", "Steven Leppard",
        "Melanie Doyle", "Tani Nath", "Vince J Kaminski", "Lucy Page", "John Smith")
    }

    "Return Nil when listType not exist" in {
      ParseEmail.extractEmailList("Cc", withMissingCc) must_=== Nil
    }
  }

  // FIXME We assume no one ever uses the following in the email body. Any inaccuracy this introduces will be minor.
  // "***********"
  // "To: "
  // "---- Forwarded by"
  "ParseEmail.extractBody" should {
    "Extract body for standard email" in {
      ParseEmail.extractBody(withEmptyCc) must_===
        """Shirley,
          |
          |Please, arrange a phone interview with Richard.
          |Stinson, myself, Vasant.
          |
          |Vince""".stripMargin
    }

    "Extract body for email with forward" in {
      ParseEmail.extractBody(withForward) must_===
        """Shirley,
          |
          |Please, arrange a phone interview with Big Data!!.
          |Stinson, myself, Vasant.
          |
          |Vince""".stripMargin
    }

    "Extract body for email with forward short" in {
      ParseEmail.extractBody(withForwardShort) must_===
        """Shirley,
          |
          |Please, arrange a phone interview with Little Data.
          |Stinson, myself, Vasant.
          |
          |Vince""".stripMargin
    }

    "Extract body for email with reply" in {
      ParseEmail.extractBody(withReply) must_=== expectedBodyWithReply
    }

    "Extract body for email with awkward reply" in {
      ParseEmail.extractBody(withAwkwardReply) must_===
        """I'm entirely not OK with this....
          |
          |Matt""".stripMargin
    }
  }

  "ParseEmail.apply" should {
    "Return an Email case class with the correct values" in {
      ParseEmail.apply(withReply) must_=== Email(
        to = List("Sophie Kingsley"),
        cc = expectedCcWithReply,
        body = expectedBodyWithReply
      )
    }
  }
}
