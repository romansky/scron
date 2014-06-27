import AssemblyKeys._

import SonatypeKeys._

// Import default settings. This changes `publishTo` settings to use the Sonatype repository and add several commands for publishing.
sonatypeSettings

assemblySettings

name := "scron"

organization := "com.uniformlyrandom"

licenses := Seq("MIT-style" -> url("http://opensource.org/licenses/mit-license.php"))

homepage := Some(url("https://github.com/uniformlyrandom/scron"))

version := "0.5"

scalaVersion := "2.11.0"

crossScalaVersions := Seq("2.10.4", "2.11.0")

publishMavenStyle := true

publishArtifact in Test := false

useGpg := true

pomIncludeRepository := { _ => false }

libraryDependencies ++= Seq(
	"org.scalatest" %% "scalatest" % "2.1.5" % "test",
	"joda-time" % "joda-time" % "2.3",
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
