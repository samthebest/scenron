
Defaults.itSettings

lazy val `it-config-sbt-project` = project.in(file(".")).configs(IntegrationTest.extend(Test))

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.12.1" % "it,test" withSources() withJavadoc(),
  "org.specs2" %% "specs2-core" % "2.4.15" % "it,test" withSources() withJavadoc(),
  "org.specs2" %% "specs2-scalacheck" % "2.4.15" % "it,test" withSources() withJavadoc(),
  //
  "org.apache.spark" %% "spark-core" % "2.1.1" withSources() withJavadoc(),
  "org.apache.spark" %% "spark-sql" % "2.1.1" withSources() withJavadoc(),
  "org.apache.spark" %% "spark-mllib" % "2.1.1" withSources() withJavadoc(),
  // We probably don't need these
  "org.scalaz" %% "scalaz-core" % "7.1.0" withSources() withJavadoc(),
  "org.apache.commons" % "commons-math3" % "3.2" withSources() withJavadoc(),
  "io.spray" %% "spray-json" % "1.3.1" withSources() withJavadoc()
)

javaOptions ++= Seq("-target", "1.8", "-source", "1.8")

name := "scenron"

parallelExecution in Test := false

version := "1"

// TODO Avro META-INF caused a deduplicate error, ought to add necessary exclude, but working that out will be a pain
// so using this brute force "shut the hell up and just compile my code" approach
mergeStrategy in assembly <<= (mergeStrategy in assembly)((old) => {
  case x if Assembly.isConfigFile(x) =>
    MergeStrategy.concat
  case PathList(ps@_*) if Assembly.isReadme(ps.last) || Assembly.isLicenseFile(ps.last) =>
    MergeStrategy.rename
  case PathList("META-INF", xs@_*) =>
    (xs map {
      _.toLowerCase
    }) match {
      case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) =>
        MergeStrategy.discard
      case ps@(x :: xs) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
        MergeStrategy.discard
      case "plexus" :: xs =>
        MergeStrategy.discard
      case "services" :: xs =>
        MergeStrategy.filterDistinctLines
      case ("spring.schemas" :: Nil) | ("spring.handlers" :: Nil) =>
        MergeStrategy.filterDistinctLines
      case _ => MergeStrategy.first // Changed deduplicate to first
    }
  case PathList(_*) => MergeStrategy.first // added this line
})

