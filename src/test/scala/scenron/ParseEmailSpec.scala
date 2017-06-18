package scenron

import org.specs2.mutable.Specification
import SampleEmails._

object ParseEmailSpec extends Specification {
  "ParseEmail.extractEmailList" should {
    "Extract empty \"Cc\" correctly" in {
      ParseEmail.extractEmailList("Cc", withEmptyCc) must_=== Nil
    }

    "Extract singleton \"To\" correctly" in {
      ParseEmail.extractEmailList("To", withForward) must_=== List("Shirley Crenshaw")
    }

    "Extract two line \"Cc\" correctly" in {
      ParseEmail.extractEmailList("Cc", withReply) must_=== List("Karen Tamlyn", "Dale Surbey", "Steven Leppard",
        "Melanie Doyle", "Tani Nath", "Vince J Kaminski", "Lucy Page")
    }

    "Extract three line \"To\" correctly" in {
      ParseEmail.extractEmailList("To", withThreeLineTo) must_=== List("Karen Tamlyn", "Dale Surbey", "Steven Leppard",
        "Melanie Doyle", "Tani Nath", "Vince J Kaminski", "Lucy Page", "John Smith")
    }

    "Return Nil when listType not exist" in {
      ParseEmail.extractEmailList("Cc", withMissingCc) must_=== Nil
    }
  }
}
