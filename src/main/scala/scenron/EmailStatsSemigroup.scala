package scenron

import scalaz.Semigroup
import scalaz.Scalaz._

// Habbit here to choose Long for counts having been in many situations where Int overflows
case class EmailStatsSemigroup(toCounts: Map[String, Long],
                               ccCounts: Map[String, Long],
                               totalBodyWords: Int,
                               numEmails: Int)

object EmailStatsSemigroup extends Semigroup[EmailStatsSemigroup] {
  def apply(email: Email): EmailStatsSemigroup = EmailStatsSemigroup(
    toCounts = email.to.map(_ -> 1l).toMap,
    ccCounts = email.cc.map(_ -> 1l).toMap,
    totalBodyWords = email.body.split("\\W+", -1).count(_.nonEmpty),
    numEmails = 1
  )

  def append(f1: EmailStatsSemigroup, f2: => EmailStatsSemigroup): EmailStatsSemigroup = EmailStatsSemigroup(
    toCounts = f1.toCounts |+| f2.toCounts,
    ccCounts = f1.ccCounts |+| f2.ccCounts,
    totalBodyWords = f1.totalBodyWords + f2.totalBodyWords,
    numEmails = f1.numEmails + f2.numEmails
  )
}
