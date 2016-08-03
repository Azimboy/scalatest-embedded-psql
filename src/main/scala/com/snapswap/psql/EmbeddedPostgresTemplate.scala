package com.snapswap.psql

import java.io.IOException
import java.net.ServerSocket
import java.util.concurrent.atomic.AtomicInteger

import org.slf4j.LoggerFactory
import ru.yandex.qatools.embed.postgresql.config.{AbstractPostgresConfig, DownloadConfigBuilder, PostgresConfig, RuntimeConfigBuilder}
import ru.yandex.qatools.embed.postgresql.distribution.Version.Main._
import ru.yandex.qatools.embed.postgresql.ext.ArtifactStoreBuilder
import ru.yandex.qatools.embed.postgresql.{Command, PostgresProcess, PostgresStarter}

class EmbeddedPostgresTemplate[T](connect: String => T, disconnect: T => Unit) {

  private lazy val (db, process) = {
    val config = new PostgresConfig(PRODUCTION, new AbstractPostgresConfig.Net("localhost", findFreePort()),
      new AbstractPostgresConfig.Storage("test"), new AbstractPostgresConfig.Timeout(),
      new AbstractPostgresConfig.Credentials("user", "password"))
    val url = "jdbc:postgresql://%s:%s/%s?user=%s&password=%s".format(
      config.net().host(),
      config.net().port(),
      config.storage().dbName(),
      config.credentials().username(),
      config.credentials().password()
    )

    val runtimeConfig = new RuntimeConfigBuilder()
      .defaults(Command.Postgres)
      .artifactStore(new ArtifactStoreBuilder()
        .defaults(Command.Postgres)
        .download(new DownloadConfigBuilder()
          .defaultsForCommand(Command.Postgres)
          .build()
        )
      ).build()
    val runtime = PostgresStarter.getInstance(runtimeConfig)
    val exec = runtime.prepare(config)

    val process = exec.start()

    val db = connect(url)

    (db, process)
  }

  private val count = new AtomicInteger()

  def open(): T = {
    count.getAndIncrement()
    db
  }

  def openCount = count.get()

  def close() = {
    LoggerFactory.getLogger(getClass).debug("Shutting down embedded PostgreSQL...")
    disconnect(db)
    process.stop()
    LoggerFactory.getLogger(getClass).debug("Embedded PostgreSQL was shutted down")
  }

  private def findFreePort(): Int = {
    var socket: ServerSocket = null
    try {
      socket = new ServerSocket(0)
      socket.setReuseAddress(true)
      val port: Int = socket.getLocalPort
      try {
        socket.close()
      } catch {
        case ignored: IOException =>
      }
      return port
    }
    catch {
      case ignored: IOException =>
    } finally {
      if (socket != null) {
        try {
          socket.close()
        } catch {
          case ignored: IOException =>
        }
      }
    }
    throw new IllegalStateException("Could not find a free TCP/IP port to start embedded Jetty HTTP Server on")
  }
}
