package com.anymessenger

import org.slf4j.{Logger, LoggerFactory}

class LoggerSpec extends UnitSpec {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  it should "print logger levels" in {
    logger.error("error")
    logger.warn("warn")
    logger.info("info")
    logger.debug("debug")
    logger.trace("trace")
  }
}
