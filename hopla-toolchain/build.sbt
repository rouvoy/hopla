name := "hopla-toolchain"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"

libraryDependencies += "com.novocode" % "junit-interface" % "0.9" % "test"

libraryDependencies += "org.specs2" %% "specs2" % "2.3.10" % "test"

unmanagedSourceDirectories in Compile += baseDirectory.value / "out"

unmanagedResourceDirectories in Test += baseDirectory.value /  "fixml-schema-4-4-20040109rev1"