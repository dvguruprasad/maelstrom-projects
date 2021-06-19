name := "maelstrom-projects"

version := "0.1"

scalaVersion := "2.13.6"

ThisBuild / scalacOptions ++= Seq("-Ymacro-annotations")

val circeVersion = "0.14.1"


libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-literal"
).map(_ % circeVersion)

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.9"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % "test"