import sbt.Keys._

lazy val scalaJSDependencies = Seq(
  libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.0"
)

val knockoutFacade = Project("knockout-facade", file("facade"))
  .settings(
    name := "knockout-facade",
    scalaVersion := "2.11.8",
    version := "1.0"
  ).settings(scalaJSDependencies)
  .enablePlugins(ScalaJSPlugin)

val knockoutExample = Project("knockout-example", file("example"))
  .settings(
    name := "knockout-example",
    scalaVersion := "2.11.8",
    version := "1.0"
  ).settings(scalaJSDependencies)
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(knockoutFacade)

val knockoutExampleBackend = Project("knockout-example-backend", file("example-backend"))
  .settings(
    name := "knockout-example-backend",
    scalaVersion := "2.11.8",
    version := "1.0",
    unmanagedResources in Compile ++= (knockoutExample.base / "target" / "scala-2.11" ** "*js").get
  ).enablePlugins(PlayScala)
  .dependsOn(knockoutExample)
