package com.anymessenger.config

import com.typesafe.config.{Config, ConfigFactory}

object TypesafeConfig {
  val config: Config = ConfigFactory
    .load()
    .withFallback(ConfigFactory.load(s"application.conf"))

  val interface: String = config.getString("http.interface")
  val port: Int = config.getInt("http.port")
}
