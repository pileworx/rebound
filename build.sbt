lazy val reboundV = "0.1.2-SNAPSHOT"
lazy val akkaHttpV = "10.1.8"
lazy val akkaV = "2.5.22"
lazy val velocityV = "2.1"
lazy val parboiledV = "2.1.6"
lazy val logbackV = "1.2.3"
lazy val scalatestV = "3.0.5"
lazy val scalamockV = "4.1.0"

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    packageName in Docker := "pileworx/rebound",
    version in Docker := reboundV,
    dockerExposedPorts := Seq(8585)
  )
  .settings(
    inThisBuild(List(
      organization := "io.pileworx",
      scalaVersion := "2.12.8",
      version := reboundV,
      scalacOptions := Seq("-feature", "-deprecation", "-encoding", "utf8")
    )),
    name := """rebound""",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpV,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
      "com.typesafe.akka" %% "akka-http-xml" % akkaHttpV,
      "com.typesafe.akka" %% "akka-stream" % akkaV,
      "com.typesafe.akka" %% "akka-slf4j" % akkaV,
      "ch.qos.logback" % "logback-classic" % logbackV % Runtime,
      "org.apache.velocity" % "velocity-engine-core" % velocityV,
      "org.parboiled" %% "parboiled" % parboiledV,

      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % Test,
      "com.typesafe.akka" %% "akka-testkit" % akkaV % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaV % Test,
      "org.scalatest" %% "scalatest" % scalatestV % Test,
      "org.scalamock" %% "scalamock" % scalamockV % Test
    )
  )
