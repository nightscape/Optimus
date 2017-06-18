import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin
import com.typesafe.sbt.SbtNativePackager.Universal
import com.typesafe.sbt.SbtNativePackager.autoImport._
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging

object OptimusBuild extends AutoPlugin {

  println {
    """
      |   /\\\\\
      |  /\\\///\\\
      | /\\\/  \///\\\    /\\\\\\\\\     /\\\       /\\\
      | /\\\      \//\\\  /\\\/////\\\ /\\\\\\\\\\\ \///    /\\\\\  /\\\\\     /\\\    /\\\  /\\\\\\\\\\
      | \/\\\       \/\\\ \/\\\\\\\\\\ \////\\\////   /\\\  /\\\///\\\\\///\\\ \/\\\   \/\\\ \/\\\//////
      |  \//\\\      /\\\  \/\\\//////     \/\\\      \/\\\ \/\\\ \//\\\  \/\\\ \/\\\   \/\\\ \/\\\\\\\\\\
      |    \///\\\  /\\\    \/\\\           \/\\\_/\\  \/\\\ \/\\\  \/\\\  \/\\\ \/\\\   \/\\\ \////////\\\
      |       \///\\\\\/     \/\\\           \//\\\\\   \/\\\ \/\\\  \/\\\  \/\\\ \//\\\\\\\\\  /\\\\\\\\\\
      |          \/////       \///             \/////    \///  \///   \///   \///  \/////////   \//////////
    """.stripMargin
  }

  override def requires: Plugins = JvmPlugin && JavaAppPackaging
  override def trigger: PluginTrigger = allRequirements

  override def projectSettings: Seq[Setting[_]] = settings

  val javaVersion: Double = sys.props("java.specification.version").toDouble

  private lazy val settings: Seq[Setting[_]] = {
    if (javaVersion < 1.8)
      sys.error("Java 8 or higher is required for building Optimus.")
    else {
      println(s"[info] Loading settings for Java $javaVersion or higher.")
      commonSettings ++ ScalaSettings ++ JavaSettings ++ PackagingOptions
    }
  }

  private val commonSettings: Seq[Setting[_]] = Seq(

    name := "Optimus",
    organization := "com.github.vagmcs",
    description := "Optimus is a mathematical programming library for Scala",
    scalaVersion := "2.11.8",

    autoScalaLibrary := true,
    managedScalaInstance := true,

    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },

    // fork a new JVM for 'run' and 'test:run'
    fork := true,

    // fork a new JVM for 'test:run', but not 'run'
    fork in Test := true,

    // add a JVM option to use when forking a JVM for 'run'
    javaOptions += "-Xmx2G",

    resolvers ++= Seq(
      Resolver.mavenLocal,
      Resolver.typesafeRepo("releases"),
      Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snapshots")
    ),

    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-library" % scalaVersion.value,
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    ),

    dependencyOverrides ++= Set(
      "org.scala-lang" % "scala-compiler" % scalaVersion.value,
      "org.scala-lang" % "scala-library" % scalaVersion.value,
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "org.scala-lang.modules" %% "scala-xml" % "1.0.5"
    ),

    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },

    // Information required in order to sync in Maven Central
    pomExtra :=
      <url>https://github.com/vagmcs</url>
      <licenses>
        <license>
          <name>GNU Lesser General Public License v3.0</name>
          <url>https://www.gnu.org/licenses/lgpl-3.0.en.html</url>
        </license>
      </licenses>
      <scm>
        <connection>scm:git:github.com/vagmcs/Optimus.git</connection>
        <developerConnection>scm:git:git@github.com:vagmcs/Optimus.git</developerConnection>
        <url>github.com/vagmcs/Optimus</url>
      </scm>
      <developers>
        <developer>
          <id>vagmcs</id>
          <name>Evangelos Michelioudakis</name>
          <url>http://users.iit.demokritos.gr/~vagmcs/</url>
        </developer>
        <developer>
          <id>anskarl</id>
          <name>Anastasios Skarlatidis</name>
          <url>http://users.iit.demokritos.gr/~anskarl/</url>
        </developer>
      </developers>
  )

  private lazy val PackagingOptions: Seq[Setting[_]] = Seq(

    // Include logger configuration file to the final distribution
    mappings in Universal ++= {
      val scriptsDir = file("core/src/main/resources/")
      scriptsDir.listFiles.toSeq.map { f =>
        f -> ("etc/" + f.getName)
      }
    },

    // File name of the universal distribution
    packageName in Universal := s"${name.value}-${version.value}"
  )

  private lazy val JavaSettings: Seq[Setting[_]] = Seq(
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-Xlint:deprecation"),

    javaOptions ++= Seq(
      "-XX:+DoEscapeAnalysis",
      "-XX:+UseFastAccessorMethods",
      "-XX:+OptimizeStringConcat",
      "-Dlogback.configurationFile=src/main/resources/logback.xml")
  )

  private lazy val ScalaSettings: Seq[Setting[_]] = Seq(
    scalacOptions ++= Seq(
      "-Yclosure-elim",
      "-Yinline",
      "-feature",
      "-target:jvm-1.8",
      "-language:implicitConversions",
      "-Ybackend:GenBCode"
    )
  )
}