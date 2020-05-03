name := "sclera-example-java-jdbc"

description := "Sclera JDBC Example and Template - Java version"

version := "4.0-SNAPSHOT"

homepage := Some(url("https://github.com/scleradb/sclera-example-java-jdbc"))

scalaVersion := "2.13.1"

licenses := Seq("MIT License" -> url("https://opensource.org/licenses/MIT"))

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
    "com.scleradb" %% "sclera-config" % "4.0",
    "com.scleradb" %% "sclera-core" % "4.0",
    "com.scleradb" %% "sclera-jdbc" % "4.0"
)

val mkscript = taskKey[File]("Create executable script")

mkscript := {
    val cp = (fullClasspath in Test).value
    val mainClassStr = "com.example.scleradb.jdbc.JdbcExample"

    val (template, scriptName) =
        if( System.getProperty("os.name").startsWith("Windows") ) (
            """|@ECHO OFF
               |java -Xmx512m -classpath "%%CLASSPATH%%:%s" %s %%*
               |""".stripMargin,
            "scleraexample.cmd"
        ) else (
            """|#!/bin/sh
               |java -Xmx512m -classpath "$CLASSPATH:%s" %s $@
               |""".stripMargin,
            "scleraexample"
        )

    val contents = template.format(cp.files.absString, mainClassStr)
    val out = baseDirectory.value / "bin" / scriptName

    IO.write(out, contents)
    out.setExecutable(true)

    out
}
