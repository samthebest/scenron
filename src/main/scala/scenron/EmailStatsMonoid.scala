package scenron

// Habbit here to choose Long for counts having been in many situations where Int overflows
case class EmailStatsMonoid(toCounts: Map[String, Long],
                            ccCounts: Map[String, Long],
                            totalBodyWords: Int,
                            numEmails: Int)

// TODO Have this extend either Scalaz or Cats monoid type so we get the syntactic sugar gratis
object EmailStatsMonoid {
  def apply(email: Email): EmailStatsMonoid = ???
}
