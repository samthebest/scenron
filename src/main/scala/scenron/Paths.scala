package scenron

// TODO Do not hardcode, inject this and deduplicate with bash scripts
object Paths {
  val emailPerRowDir = "file:/enron/email_per_row"
  val emailPerRowDirAlt = "file:/enron/email_per_row_alt"
  val filePattern = "/edrm-enron-v2*/text*"
  val unzipped = "/enron/flat"
}
