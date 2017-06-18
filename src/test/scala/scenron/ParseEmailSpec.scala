package scenron

import org.specs2.mutable.Specification
import SampleEmails._

object ParseEmailSpec extends Specification {
  "ParseEmail.extractEmailList" should {
    "extract empty \"Cc\" correctly" in {
      ParseEmail.extractEmailList("Cc", withEmptyCc) must_=== Nil
    }
  }
}
