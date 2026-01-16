resolvers += "HMRC-open-artefacts-maven" at "https://open.artefacts.tax.service.gov.uk/maven2"
resolvers += Resolver.url("HMRC-open-artefacts-ivy", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(
  Resolver.ivyStylePatterns
)

addSbtPlugin("io.gatling"     % "gatling-sbt"            % "4.9.2")
addSbtPlugin("org.jmotor.sbt" % "sbt-dependency-updates" % "1.2.9")
addSbtPlugin("org.scalameta"  % "sbt-scalafmt"           % "2.5.6")
addSbtPlugin("uk.gov.hmrc"    % "sbt-auto-build"         % "3.24.0")

// Scala module 2.16.1 requires Jackson Databind version >= 2.16.0 and < 2.17.0
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.16.1"
