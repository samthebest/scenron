package scenron

case class Email(to: List[String], cc: List[String], body: String)

object ParseEmail {
  def extractEmailList(listType: String, email: String): List[String] =
    email.split("\n", -1).dropWhile(!_.startsWith(listType + ":")).toList
    .takeWhile(s => s.startsWith(listType + ":") || s.startsWith("   "))
    .map(_.stripPrefix(listType + ":").trim).filter(_.nonEmpty).mkString(" ")
    .split(", ", -1).toList.filter(_.nonEmpty)

  def extractBody(email: String): String =
    email.split("\n", -1).dropWhile(!_.startsWith("X-ZLID:")).drop(1)
    .sliding(4).map(_.toList).takeWhile(l => !l.last.startsWith("To: ")).map(_.head)
    .takeWhile(s => s != "***********" && !s.contains("----- Forwarded by"))
    .mkString("\n").trim()

  def apply(email: String): Email = Email(
    to = extractEmailList("To", email),
    cc = extractEmailList("Cc", email),
    extractBody(email)
  )
}
