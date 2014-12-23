name := "Optimus"

version := "1.0"

scalaVersion := "2.10.4"

autoScalaLibrary := true

managedScalaInstance := true

// Append several options to the list of options passed to the Java compiler
javacOptions ++= Seq("-source", "1.7", "-target", "1.7", "-Xlint:unchecked", "-Xlint:deprecation")

// Append scalac options
scalacOptions ++= Seq(
  "-optimise",
  "-Yclosure-elim",
  "-Yinline",
  "-feature",
  "-target:jvm-1.7",
  "-language:implicitConversions"
)

// fork a new JVM for 'run' and 'test:run'
fork := true

// fork a new JVM for 'test:run', but not 'run'
fork in Test := true

// add a JVM option to use when forking a JVM for 'run'
javaOptions += "-Xmx1G"

// Unit testing
libraryDependencies ++= Seq(
  "junit" % "junit" % "4.11" % "test",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)