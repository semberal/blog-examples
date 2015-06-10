organization := "eu.semberal"

name := "scala-future-timeout-patterns"

version := "1.0"

scalaVersion := "2.11.2"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings", "-Xmax-classfile-name", "140")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.0" % "test",
  "com.typesafe.akka" %% "akka-actor" % "2.3.4" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.4" % "test"
)
