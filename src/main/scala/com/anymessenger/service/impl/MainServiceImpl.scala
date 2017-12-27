package com.anymessenger.service.impl

import com.anymessenger.model.Tables._
import com.anymessenger.service.api.MainService
import db.DBConfig
import org.slf4j.{Logger, LoggerFactory}
import util.logTimeF

import scala.concurrent.{ExecutionContext, Future}

object MainServiceImpl
  extends MainService
    with DBConfig {

  private implicit val logger: Logger = LoggerFactory.getLogger(this.getClass)

  import profile.api._

  def getUser(userid: Long)(implicit executor: ExecutionContext): Future[Either[String, Option[UserRow]]] =
    logTimeF(s"getUser(customerId = $userid)") {
      db.run(User.filter(user =>
        user.id === userid &&
          !user.isdeleted &&
          user.isactive
      ).result)
        .map { res => Right(res.headOption)
        }
    }
      .recover {
        case e: Throwable =>
          Left(e.getMessage)
      }

  def getMessage(messageId: Long)(implicit executor: ExecutionContext): Future[Either[String, Option[MessageRow]]] =
    logTimeF(s"getMessage(messageId = $messageId)") {
      db.run(Message.filter(message =>
        message.id === messageId &&
          !message.isdeleted
      ).result)
        .map { res => Right(res.headOption) }
    }
      .recover {
        case e: Throwable =>
          Left(e.getMessage)
      }


  def getUserMessages(userid: Long)(implicit executor: ExecutionContext): Future[Either[String, List[MessageRow]]] =
    logTimeF(s"getUserMessage(userid = $userid)") {
      db.run(Message.filter(message =>
        message.userid === userid &&
          !message.isdeleted
      ).result)
        .map { res => Right(res.toList) }
    }
      .recover {
        case e: Throwable =>
          Left(e.getMessage)
      }
}
