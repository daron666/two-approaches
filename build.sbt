ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "two-approaches",
    libraryDependencies ++= Seq("dev.zio" %% "zio" % "1.0.12", "io.d11" %% "zhttp" % "1.0.0.0-RC23")
  )
