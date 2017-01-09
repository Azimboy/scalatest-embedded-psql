name := "scalatest-embedded-psql"

organization := "com.snapswap"

version := "0.0.6"

scalaVersion := "2.11.7"

scalacOptions := Seq(
  "-feature",
  "-unchecked",
  "-deprecation",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Xfuture",
  "-Ywarn-unused-import",
  "-encoding",
  "UTF-8")

resolvers ++= Seq(
  "SnapSwap repo" at "https://dev.snapswap.vc/artifactory/libs-release",
  "SnapSwap snapshot repo" at "https://dev.snapswap.vc/artifactory/libs-snapshot/"
)

libraryDependencies ++= {
  val slickV = "3.1.1"
  Seq(
    "org.scalatest" %% "scalatest" % "3.0.1",
    "ru.yandex.qatools.embed" % "postgresql-embedded" % "1.19",
    "org.reflections" % "reflections" % "0.9.10"
  )
}

fork in Test := true

javaOptions in Test += "-Xmx512m"

testOptions in Test += Tests.Argument("-u", "console")
