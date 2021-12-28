
// Import default settings. This changes `publishTo` settings to use the Sonatype repository and add several commands for publishing.
xerial.sbt.Sonatype.sonatypeSettings

name := "scron"

organization := "com.uniformlyrandom"

licenses := Seq("MIT-style" -> url("http://opensource.org/licenses/mit-license.php"))

homepage := Some(url("https://github.com/uniformlyrandom/scron"))

version := "1.0.2"

scalaVersion := "2.13.7"

publishMavenStyle := true

publishArtifact in Test := false

//useGpg := true

pomIncludeRepository := { _ => false }

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.2" % Test,
  "org.scala-lang" % "scala-reflect" % scalaVersion.value
)

resolvers ++= Seq(
	"Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
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
