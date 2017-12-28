package com.anymessenger.service.impl

import com.anymessenger.model.Tables._
import com.anymessenger.model.Tables.profile.api._
import com.anymessenger.service.api.MainService
import com.typesafe.scalalogging.{LazyLogging, Logger}
import config.Slicker
import util.logTimeF

import scala.concurrent.{ExecutionContext, Future}

object MainServiceImpl
  extends MainService
    with Slicker
    with LazyLogging {

  implicit val _logger: Logger = logger

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
