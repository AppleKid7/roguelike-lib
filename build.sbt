import scala.sys.process._
import scala.language.postfixOps

Global / onChangedBuildSource := ReloadOnSourceChanges

val scala3Version = "3.0.0"

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin, SbtIndigo)
  .settings(
    name := "roguelike-lib",
    version := "0.0.1",
    scalaVersion := scala3Version,
    organization := "io.indigoengine",
    libraryDependencies ++= Seq(
      "org.scalameta" %%% "munit" % "0.7.26" % Test
    ),
    testFrameworks += new TestFramework("munit.Framework"),
    Test / scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
    showCursor := true,
    libraryDependencies ++= Seq(
      "io.indigoengine" %%% "indigo-json-circe" % "0.9.0",
      "io.indigoengine" %%% "indigo" % "0.9.0",
      "io.indigoengine" %%% "indigo-extras" % "0.9.0"
    )
  )
  .settings(
    Compile / sourceGenerators += Def.task {
      TileCharGen
        .gen(
          "DfTiles", // Class/module name.
          "indigo.lib.roguelike", // fully qualified package name
          (Compile / sourceManaged).value, // Managed sources (output) directory for the generated classes
          10, // Character width
          10 // Character height
        )
    }.taskValue
  )
  .settings(
    code := { "code ." ! }
  )

lazy val code =
  taskKey[Unit]("Launch VSCode in the current directory")
