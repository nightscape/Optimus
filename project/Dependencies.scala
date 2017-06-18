import sbt._

object Dependencies {

  final val LogbackVersion = "1.1.7"
  final val SLF4JVersion = "1.7.21"
  final val ScalaLogging = "3.5.0"
  final val ScalaTestVersion = "2.2.6"
  final val ScalaMeterVersion = "0.8.2"
  final val LpSolveVersion = "5.5.2.0"
  final val ojAlgorithmsVersion = "43.0"

  // Logging using slf4j and logback
  lazy val Logging = Seq(
    "ch.qos.logback" % "logback-classic" % LogbackVersion,
    "ch.qos.logback" % "logback-core" % LogbackVersion,
    "org.slf4j" % "slf4j-api" % SLF4JVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % ScalaLogging
  )

  // ScalaTest and ScalaMeter for UNIT testing
  lazy val ScalaTest = Seq(
    "org.scalatest" %% "scalatest" % ScalaTestVersion % "test",
    "com.storm-enroute" %% "scalameter" % ScalaMeterVersion % "test"
  )

  // GNU Trove collections for high performance and memory efficient data structures
  lazy val Trove: ModuleID = "net.sf.trove4j" % "trove4j" % "3.0.3"

  // LpSolve library for linear programming
  lazy val LpSolve: ModuleID = "com.datumbox" % "lpsolve" % LpSolveVersion

  // oj! Algorithms library for linear and quadratic programming
  lazy val ojAlgorithms: ModuleID = "org.ojalgo" % "ojalgo" % ojAlgorithmsVersion
}