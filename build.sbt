crossScalaVersions := Seq("2.11.12", "2.12.4")

lazy val commonSettings = Seq(
  scalaVersion := "2.12.4",

scalacOptions ++=
  "-encoding" :: "UTF-8" ::
  "-unchecked" ::
  "-deprecation" ::
  "-explaintypes" ::
  "-feature" ::
  "-language:_" ::
  "-Xfuture" ::
  // "-Xlint" ::
  "-Ypartial-unification" ::
  "-Yno-adapted-args" ::
  "-Ywarn-infer-any" ::
  "-Ywarn-value-discard" ::
  "-Ywarn-nullary-override" ::
  "-Ywarn-nullary-unit" ::
  "-P:scalajs:sjsDefinedByDefault" ::
    Nil,

scalacOptions ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 12)) =>
      "-Ywarn-extra-implicit" ::
      // "-Ywarn-unused:-explicits,-implicits,_" ::
      Nil
    case _             =>
      "-Ywarn-unused" ::
      Nil
    }
  }
)

val catsVersion = "1.0.1"
val catsEffectVersion = "0.8"

// Not a Monad for reactive programming
lazy val nomad = project
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "com.github.mpilquist" %% "simulacrum" % "0.11.0" % "provided",
      "org.typelevel" %%% "cats-core" % catsVersion,
          "org.typelevel" %%% "cats-effect" % catsEffectVersion
    ),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
  )



lazy val outwatchRxjs = (project in file("outwatch-rxjs"))
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(outwatch, nomad)
    .settings(commonSettings)
    .settings(
        libraryDependencies ++= Seq(
          "org.typelevel" %%% "cats-core" % catsVersion,
          "org.typelevel" %%% "cats-effect" % catsEffectVersion,
          "com.github.lukajcb" %%% "rxscala-js" % "0.15.2",
          "org.scalatest" %%% "scalatest" % "3.0.4" % Test,
          "org.scalacheck" %%% "scalacheck" % "1.13.5" % Test
        )
      )


lazy val outwatch = project
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
  .settings(commonSettings)
  .dependsOn(nomad)
  .settings(
    name := "OutWatch",
    normalizedName := "outwatch",
    version := "0.11.1-SNAPSHOT",
    organization := "io.github.outwatch",
    libraryDependencies ++= Seq(
      "com.raquo" %%% "domtypes" % "0.4.2",
      "org.typelevel" %%% "cats-core" % catsVersion,
      "org.typelevel" %%% "cats-effect" % catsEffectVersion,
      "org.scalatest" %%% "scalatest" % "3.0.4" % Test,
      "org.scalacheck" %%% "scalacheck" % "1.13.5" % Test
    ),
    npmDependencies in Compile ++= Seq(
      "rxjs" -> "5.4.3",
      "snabbdom" -> "0.7.0"
    ),

    requiresDOM in Test := true,
    useYarn := true,

    publishMavenStyle := true,

    licenses += ("Apache 2", url("https://www.apache.org/licenses/LICENSE-2.0.txt")),

    homepage := Some(url("https://outwatch.github.io/")),

scmInfo := Some(ScmInfo(
  url("https://github.com/OutWatch/outwatch"),
  "scm:git:git@github.com:OutWatch/outwatch.git",
      Some("scm:git:git@github.com:OutWatch/outwatch.git")
    )),

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },


pomExtra :=
  <developers>
    <developer>
      <id>ltj</id>
      <name>Luka Jacobowitz</name>
      <url>https://github.com/LukaJCB</url>
    </developer>
      </developers>,



pomIncludeRepository := { _ => false }
  )
