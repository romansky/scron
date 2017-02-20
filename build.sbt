import AssemblyKeys._

// Import default settings. This changes `publishTo` settings to use the Sonatype repository and add several commands for publishing.
xerial.sbt.Sonatype.sonatypeSettings

assemblySettings

name := "scron"

organization := "com.uniformlyrandom"

licenses := Seq("MIT-style" -> url("http://opensource.org/licenses/mit-license.php"))

homepage := Some(url("https://github.com/uniformlyrandom/scron"))

version := "0.6.0"

scalaVersion := "2.12.1"

crossScalaVersions := Seq("2.11.6", "2.12.1")

publishMavenStyle := true

publishArtifact in Test := false

//useGpg := true

pomIncludeRepository := { _ => false }

libraryDependencies ++= Seq(
	"org.scalatest" %% "scalatest" % "3.0.1" % "test",
	"joda-time" % "joda-time" % "2.6",
	"org.joda" % "joda-convert" % "1.6"
)

libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _)

resolvers ++= Seq(
	"Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/" 
)


pomExtra := (
  <scm>
    <url>git@github.com:uniformlyrandom/scron.git</url>
    <connection>scm:git:git@github.com:uniformlyrandom/scron.git</connection>
  </scm>
  <developers>
    <developer>
      <id>romansky</id>
      <name>Roman Landenband</name>
    </developer>
  </developers>)
