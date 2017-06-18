
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
