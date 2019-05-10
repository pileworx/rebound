lazy val reboundVersion = "0.1.1"
lazy val akkaHttpVersion = "10.1.7"
lazy val akkaVersion = "2.5.21"
lazy val velocityVersion = "2.1"
lazy val scalatestVersion = "3.0.5"
lazy val scalamockVersion = "4.1.0"

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    packageName in Docker := "pileworx/rebound",
    version in Docker := reboundVersion,
    dockerExposedPorts := Seq(8585)
  )
  .settings(
    inThisBuild(List(
      organization := "io.pileworx",
      scalaVersion := "2.12.8",
      version := reboundVersion,
      scalacOptions := Seq("-feature", "-deprecation", "-encoding", "utf8")
    )),
    name := """rebound""",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "org.apache.velocity" % "velocity-engine-core" % velocityVersion,

      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "org.scalatest" %% "scalatest" % scalatestVersion % Test,
      "org.scalamock" %% "scalamock" % scalamockVersion % Test
    )
  )
