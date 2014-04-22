organization := "com.osinka.slugify"

name := "slugify"

homepage := Some(url("https://github.com/osinka/slugify"))

startYear := Some(2013)

scalaVersion := "2.11.0"

crossScalaVersions := Seq("2.11.0", "2.10.4")

licenses += "Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")

organizationName := "Osinka"

description := """Making slugs out of any string"""

scalacOptions ++= List("-deprecation", "-unchecked", "-feature")

libraryDependencies ++= Seq(
  "com.ibm.icu"		%  "icu4j"	% "53.1",
  "org.scalatest"	%% "scalatest"	% "2.1.3"	% "test",
  "org.scalacheck"	%% "scalacheck"	% "1.11.3"	% "test"
)

credentials <+= (version) map { version: String =>
  val file =
    if (version.trim endsWith "SNAPSHOT") "credentials_osinka"
    else "credentials_sonatype"
  Credentials(Path.userHome / ".ivy2" / file)
}

pomIncludeRepository := { x => false }

publishTo <<= (version) { version: String =>
  Some(
    if (version.trim endsWith "SNAPSHOT")
      "Osinka Internal Repo" at "http://repo.osinka.int/content/repositories/snapshots/"
    else
      "Sonatype OSS Staging" at "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
  )
}

useGpg := true

pomExtra := <xml:group>
    <developers>
      <developer>
        <id>alaz</id>
        <email>azarov@osinka.com</email>
        <name>Alexander Azarov</name>
        <timezone>+4</timezone>
      </developer>
    </developers>
    <scm>
      <connection>scm:git:git://github.com/osinka/slugify.git</connection>
      <developerConnection>scm:git:git@github.com:osinka/slugify.git</developerConnection>
      <url>http://github.com/osinka/slugify</url>
    </scm>
    <issueManagement>
      <system>github</system>
      <url>http://github.com/osinka/slugify/issues</url>
    </issueManagement>
  </xml:group>
