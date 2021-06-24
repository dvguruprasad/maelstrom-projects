import sbt.Keys.libraryDependencies

ThisBuild / organization := "org.maelstromprojects"

ThisBuild /version := "0.1.0"

ThisBuild / scalaVersion := "2.13.6"

ThisBuild / scalacOptions ++= Seq("-Ymacro-annotations")

val circeVersion = "0.14.1"

val circe = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-literal"
).map(_ % circeVersion)
val scalactic = "org.scalactic" %% "scalactic" % "3.2.9"
val scalatest = "org.scalatest" %% "scalatest" % "3.2.9" % Test

lazy val maelstromProject = (project in file("."))
  .settings(
    name := "maelstrom-projects",
    libraryDependencies ++= (circe ++ Seq(scalactic, scalatest))
  )

