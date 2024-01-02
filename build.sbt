// Import default settings. This changes `publishTo` settings to use the Sonatype repository and add several commands for publishing.
xerial.sbt.Sonatype.sonatypeSettings

name := "scron"

version := CommonVersions.scron
scalaVersion := CommonVersions.scala

organization := "com.uniformlyrandom"
licenses := Seq("MIT-style" -> url("https://opensource.org/licenses/mit-license.php"))
homepage := Some(url("https://github.com/uniformlyrandom/scron"))
publishMavenStyle := true
Test / publishArtifact := false
//useGpg := true
pomIncludeRepository := { _ => false }
credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
publishTo := sonatypePublishToBundle.value
publishConfiguration := publishConfiguration.value.withOverwrite(true)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % CommonVersions.scalaTest % Test,
  "org.scala-lang" % "scala-reflect" % scalaVersion.value
)
resolvers ++= Seq(
  "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

pomExtra := <scm>
  <url>git@github.com:uniformlyrandom/scron.git</url>
  <connection>scm:git:git@github.com:uniformlyrandom/scron.git</connection>
</scm>
  <developers>
    <developer>
      <id>romansky</id>
      <name>Roman Landenband</name>
    </developer>
  </developers>
