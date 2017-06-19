package scenron

import org.specs2.mutable.Specification

object FullSampledRunIT extends Specification {
  "The scripts together with the App" should {
    // TODO We should create a snapshot with a reasonable sampled version of the data
    // then run the entire end to end (i.e. cluster creation scripts + prep + stats App) pointed at the sampled snapshot
    // as part of this test.  This would serve as a full E2E/IT test.  It would be quite slow so should be part of a
    // nightly build rather than a per-push CI build.
    "Produce the correct output when using the sampled snapshot ID" in {
      pending
    }
  }
}
