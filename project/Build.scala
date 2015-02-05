import sbt._
import Keys._

object HoplaBuild extends Build {
  lazy val root = project.in(file(".")).aggregate(hopla, generatedxhtml, hoplaxhtml)
  lazy val hopla = project.in(file("hopla"))
  lazy val generatedxhtml = project.in(file("target/generated")).dependsOn(hopla)
  lazy val hoplaxhtml = project.in(file("hopla-xhtml")).dependsOn(generatedxhtml)
}