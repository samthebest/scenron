package scenron

object ParseEmail {
  def extractEmailList(listType: String, email: String): List[String] = 
    email.split("\n", -1).dropWhile(!_.startsWith(listType + ":")).toList
    .takeWhile(s => s.startsWith(listType + ":") || s.startsWith("   "))
    .map(_.stripPrefix(listType + ":").trim).filter(_.nonEmpty).mkString(" ")
    .split(", ", -1).toList.filter(_.nonEmpty)
}
