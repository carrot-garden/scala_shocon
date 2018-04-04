
// https://github.com/portable-scala/sbt-crossproject
// shadow sbt-scalajs' crossProject and CrossType until Scala.js 1.0.0 is released
import sbtcrossproject.{crossProject, CrossType}

import xerial.sbt.Sonatype._

val commonSettingsJVM = Seq(
)

val commonSettingsJS = Seq(
  libraryDependencies += "org.scala-js" %%% "scalajs-java-time" % "0.2.0",
  parallelExecution in Test := true
)

val commonSettingsNative = Seq(
    nativeGC := "none",
    nativeMode := "debug",
    nativeLinkStubs := false,  
    crossScalaVersions := Seq("2.11.12") // no 2.12.X
)

lazy val root = project
  .in(file("."))
  .aggregate(parserJS, parserJVM, parserNative, facadeJS, facadeJVM, facadeNative)

lazy val fixResources =
  taskKey[Unit]("Fix application.conf presence on first clean build.")

lazy val parser = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("."))
  .settings(
    name := "shocon-parser",
    scalacOptions ++=
      Seq(
        "-feature",
        "-unchecked",
        "-language:implicitConversions"
      )
  )
  .settings(sonatypeSettings)
  .settings(
    fixResources := {
      val compileConf = (resourceDirectory in Compile).value / "application.conf"
      if (compileConf.exists)
        IO.copyFile(
          compileConf,
          (classDirectory in Compile).value / "application.conf"
        )
      val testConf = (resourceDirectory in Test).value / "application.conf"
      if (testConf.exists) {
        IO.copyFile(
          testConf,
          (classDirectory in Test).value / "application.conf"
        )
      }
    },
    compile in Compile := (compile in Compile).dependsOn(fixResources).value,
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "fastparse" % "1.0.0",
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % "provided"
    )
  )
  .jvmSettings(commonSettingsJVM)
  .jsSettings(commonSettingsJS)
  .nativeSettings(commonSettingsNative)

lazy val parserJVM = parser.jvm
lazy val parserJS = parser.js
lazy val parserNative = parser.native

lazy val facade = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("facade"))
  .dependsOn(parser)
  .settings(
    name := "shocon",
    scalacOptions ++=
      Seq(
        "-feature",
        "-unchecked",
        "-language:implicitConversions"
      )
  )
  .settings(sonatypeSettings)
  .settings(
    fixResources := {
      val compileConf = (resourceDirectory in Compile).value / "application.conf"
      if (compileConf.exists)
        IO.copyFile(
          compileConf,
          (classDirectory in Compile).value / "application.conf"
        )
      val testConf = (resourceDirectory in Test).value / "application.conf"
      if (testConf.exists) {
        IO.copyFile(
          testConf,
          (classDirectory in Test).value / "application.conf"
        )
      }
    },
    compile in Compile := (compile in Compile).dependsOn(fixResources).value,
    testFrameworks += new TestFramework("utest.runner.Framework"),
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "fastparse" % "1.0.0",
      "com.lihaoyi" %%% "utest" % "0.6.3" % "test",
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % "provided"
    )
  )
  .jvmSettings(commonSettingsJVM)
  .jsSettings(commonSettingsJS)
  .nativeSettings(commonSettingsNative)

lazy val facadeJVM = facade.jvm
lazy val facadeJS = facade.js
lazy val facadeNative = facade.native
