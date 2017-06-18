import sbt.Keys._

addCommandAlias("build", ";compile;test;package")
addCommandAlias("rebuild", ";clean;build")

sonatypeProfileName := "com.github.vagmcs"

lazy val root = Project("optimus-assembly", file("."))
  .aggregate(core, oj, lpsolve, gurobi, mosek)
  .enablePlugins(JavaAppPackaging)
  .settings(logLevel in Test := Level.Info)
  .settings(logLevel in Compile := Level.Error)
  .settings(name := "optimus")
  .settings(publish := { })
  .settings(publishLocal := { })

publishArtifact in root := false

// Build settings for Optimus core
lazy val core = Project("optimus-core", file("core"))
  .enablePlugins(JavaAppPackaging)
  .settings(logLevel in Test := Level.Info)
  .settings(logLevel in Compile := Level.Error)
  .settings(name := "optimus-core")
  .settings(Seq(
    libraryDependencies ++= Dependencies.Logging,
    libraryDependencies ++= Dependencies.ScalaTest,
    libraryDependencies += Dependencies.Trove
  ))

// Build settings for Optimus oj solver
lazy val oj = Project("solver-oj", file("solver-oj"))
  .dependsOn(core % "compile->compile ; test->test")
  .enablePlugins(JavaAppPackaging)
  .settings(logLevel in Test := Level.Info)
  .settings(logLevel in Compile := Level.Error)
  .settings(name := "optimus-solver-oj")
  .settings(libraryDependencies += Dependencies.ojAlgorithms)

// Build settings for Optimus lp solver
lazy val lpsolve = Project("solver-lp", file("solver-lp"))
  .dependsOn(core % "compile->compile ; test->test")
  .enablePlugins(JavaAppPackaging)
  .settings(logLevel in Test := Level.Info)
  .settings(logLevel in Compile := Level.Error)
  .settings(name := "optimus-solver-lp")
  .settings(libraryDependencies += Dependencies.LpSolve)

// Build settings for Optimus gurobi solver
lazy val gurobi = if (file("lib/gurobi.jar").exists)
  Project("solver-gurobi", file("solver-gurobi"))
    .dependsOn(core % "compile->compile ; test->test")
    .enablePlugins(JavaAppPackaging)
    .settings(logLevel in Test := Level.Info)
    .settings(logLevel in Compile := Level.Error)
    .settings(name := "optimus-solver-gurobi")
    .settings(unmanagedJars in Compile += file("lib/gurobi.jar"))
else
  Project("solver-gurobi", file("solver-gurobi"))
    .settings(unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)( _ :: Nil))
    .settings({
    println(s"[warn] Building in the absence of support for the Gurobi solver " +
            s"[ 'gurobi.jar' not found in 'lib' directory ].")
    Seq(name := "optimus-solver-gurobi", publish := { }, publishLocal := { })
  })

// Build settings for Optimus mosek solver
lazy val mosek = if (file("lib/mosek.jar").exists)
    Project("solver-mosek", file("solver-mosek"))
      .dependsOn(core % "compile->compile ; test->test")
      .enablePlugins(JavaAppPackaging)
      .settings(logLevel in Test := Level.Info)
      .settings(logLevel in Compile := Level.Error)
      .settings(name := "optimus-solver-mosek")
      .settings(unmanagedJars in Compile += file("lib/mosek.jar"))
else
  Project("solver-mosek", file("solver-mosek"))
    .settings({
      println(s"[warn] Building in the absence of support for the Mosek solver " +
              s"[ 'mosek.jar' not found in 'lib' directory ].")
      Seq(name := "optimus-solver-mosek", publish := { }, publishLocal := { })
    })