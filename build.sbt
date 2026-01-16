lazy val root = (project in file("."))
  .enablePlugins(GatlingPlugin)
  .settings(
    name := "universal-credit-liability-performance-tests",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.13.18",
    // implicitConversions & postfixOps are Gatling recommended -language settings
    scalacOptions ++= Seq("-feature", "-language:implicitConversions", "-language:postfixOps"),
    // Enabling sbt-auto-build plugin provides DefaultBuildSettings with default `testOptions` from `sbt-settings` plugin.
    // These testOptions are not compatible with `sbt gatling:test`. So we have to override testOptions here.
    Test / testOptions := Seq.empty,
    // JVM args for Java 17/21 compatibility
    // Resolves issue "IllegalAccessException: module java.base does not open java.lang to unnamed module"
    // https://github.com/gatling/gatling/issues/4599
    Gatling / javaOptions ++= Seq(
      "--add-opens=java.base/java.lang=ALL-UNNAMED",
      "--add-opens=java.base/java.io=ALL-UNNAMED"
    ),
    libraryDependencies ++= Dependencies.test,
    // Scalafix / SemanticDB settings
    scalafixConfigSettings(Gatling),
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision // only required for Scala 2.x
  )

addCommandAlias("prePrChecks", "; scalafmtCheckAll; scalafmtSbtCheck; scalafixAll --check")
addCommandAlias("lintCode", "; scalafmtAll; scalafmtSbt; scalafixAll")
