name := "proton-users"

libraryDependencies ++= List(
  Library.typesafeConfig,
  Library.slf4j,
  Library.logback,
  Library.scalaLogging,
  Library.akkaActor,
  Library.akkaSlf4j,
  Library.akkaHttpCore,
  Library.akkaHttp,
  Library.akkaHttpSprayJson,
  Library.scopt,
  Library.scaldi,
  Library.slick,
  Library.slickHikariCP,
  Library.slickPG,
  Library.hikariCP,
  Library.postgresql,
  Library.flyway,
  Library.scalaTest         % "test",
  Library.akkaTestKit       % "test"
)