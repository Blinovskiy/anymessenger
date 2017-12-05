package service

import org.slf4j.{Logger, LoggerFactory}

object CheckLogger extends App {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  logger.error("error")
  logger.warn("warn")
  logger.info("info")
  logger.debug("debug")
  logger.trace("trace")

}
