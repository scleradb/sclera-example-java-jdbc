name := "sclera-example-scala-jdbc"

description := "Sclera JDBC Example and Template"

version := "4.0-SNAPSHOT"

homepage := Some(url("https://github.com/scleradb/sclera-example-jdbc"))

scalaVersion := "2.13.1"

licenses := Seq("MIT License" -> url("https://opensource.org/licenses/MIT"))

libraryDependencies ++= Seq(
    "com.scleradb" %% "sclera-config" % "4.0-SNAPSHOT",
    "com.scleradb" %% "sclera-core" % "4.0-SNAPSHOT",
    "com.scleradb" %% "sclera-jdbc" % "4.0-SNAPSHOT"
)

// uncomment the line below to set SCLERA_ROOT
// if set, this will be used instead of the environment variable
// javaOptions += "-DSCLERA_ROOT=/path/to/sclera_root"

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
