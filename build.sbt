import AssemblyKeys._

assemblySettings

name := "scron"

organization := "com.uniformlyrandom"

version := "0.5"

scalaVersion := "2.11.0"

crossScalaVersions := Seq("2.10.4", "2.11.0")

publishMavenStyle := true

libraryDependencies ++= Seq(
	"org.scalatest" %% "scalatest" % "2.1.5" % "test",
	"joda-time" % "joda-time" % "2.3",
	"org.joda" % "joda-convert" % "1.6"
)

libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _)

resolvers ++= Seq(
	"jboss repo" at "http://repository.jboss.org/nexus/content/groups/public-jboss/",
	"Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/" 
)

publishArtifact in Test := false

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}
