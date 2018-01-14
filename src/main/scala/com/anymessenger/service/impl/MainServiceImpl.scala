package com.anymessenger.service.impl

import java.sql.Timestamp

import com.anymessenger.config.Slicker
import com.anymessenger.dbmodel.Tables._
import com.anymessenger.dbmodel.Tables.profile.api._
import com.anymessenger.service.api.{ReadingServices, WritingServices}
import com.anymessenger.service.model.MessageEntity
import com.anymessenger.util.{logTimeF, nowTimestamp}
import com.typesafe.scalalogging.{LazyLogging, Logger}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

object MainServiceImpl
  extends ReadingServices
    with WritingServices
    with Slicker
    with LazyLogging {

  implicit val _logger: Logger = logger

  def checkConnection()(implicit executor: ExecutionContext): Future[Either[Throwable, Option[Long]]] =
    logTimeF(s"checkConnection()") {
      db.run(sql"select 1".as[Long].headOption)
        .map { res => Right(res) }
    }
      .recover {
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)
      }

  def getUser(userid: Long)(implicit executor: ExecutionContext): Future[Either[Throwable, Option[UserRow]]] =
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
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)
      }

  def getMessage(messageId: Long)(implicit executor: ExecutionContext): Future[Either[Throwable, Option[MessageRow]]] =
    logTimeF(s"getMessage(messageId = $messageId)") {
      db.run(Message.filter(message =>
        message.id === messageId &&
          !message.isdeleted
      ).result)
        .map { res => Right(res.headOption) }
    }
      .recover {
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)

      }

  def getUserMessages(userid: Long)(implicit executor: ExecutionContext): Future[Either[Throwable, List[MessageRow]]] =
    logTimeF(s"getUserMessage(userid = $userid)") {
      db.run(Message.filter(message =>
        message.userid === userid &&
          !message.isdeleted
      )
        .sortBy(_.createdat)
        .result)
        .map { res => Right(res.toList) }
    }
      .recover {
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)
      }

  def getAllMessages()(implicit executor: ExecutionContext): Future[Either[Throwable, List[MessageRow]]] =
    logTimeF(s"getAllMessages()") {
      db.run(Message.filter(!_.isdeleted).sortBy(_.createdat).result)
        .map { res => Right(res.toList) }
    }
      .recover {
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)
      }


  def getMessagesByDate(from: Timestamp, to: Timestamp)(implicit executor: ExecutionContext): Future[Either[Throwable, List[MessageRow]]] =
    logTimeF(s"getMessagesByDate(from = $from, to = $to)") {
      db.run(Message
        .filter(f => !f.isdeleted && f.createdat >= from && f.createdat <= to)
        .sortBy(_.createdat)
        .result)
        .map { res => Right(res.toList) }
    }
      .recover {
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)
      }

  def getLastNMessages(n: Int)(implicit executor: ExecutionContext): Future[Either[Throwable, List[MessageRow]]] =
    logTimeF(s"getLastNMessages(n = $n)") {
      db.run(Message
        .filter(f => !f.isdeleted)
        .sortBy(_.createdat.desc)
        .take(n)
        .sortBy(_.createdat.asc)
        .result)
        .map { res => Right(res.toList) }
    }
      .recover {
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)
      }


  def insertOrUpdateMessage(messageEntity: MessageEntity)(implicit executor: ExecutionContext): Future[Either[Throwable, Long]] = {
    logTimeF(s"insertOrUpdateMessage(messageEntity = $messageEntity)") {
      messageEntity match {
        // insert
        case MessageEntity(None | Some(-1L), text, userId) =>
          db.run(
            Message.map(m => (m.text, m.userid, m.createdat)) returning Message.map(_.id) += (Some(text), Some(userId), Some(nowTimestamp))
          )
            .map(Right(_))

        // update
        case MessageEntity(Some(id), text, userId) =>
          db.run(
            Message
              .filter(_.id === id)
              .map(m => (m.text, m.userid, m.updatedat))
              .update((Some(text), Some(userId), Some(nowTimestamp)))
          )
            .map(f =>
              if (f == 1) Right(id)
              else Left(new Throwable(s"Can't update message [id=$id]"))
            )
      }
    }
      .recover {
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)
      }
  }

  def deleteMessage(id: Long)(implicit executor: ExecutionContext): Future[Either[Throwable, Long]] = {
    db.run(
      Message
        .filter(_.id === id)
        .map(m => (m.isdeleted, m.deletedat))
        .update((true, Some(nowTimestamp)))
    )
      .map(f =>
        if (f == 1) Right(id)
        else Left(new Throwable(s"Can't update message [id=$id]"))
      )
      .recover {
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)
      }
  }

  //  def insertMessage(message: MessageRow)(implicit executor: ExecutionContext): Future[Either[Throwable, Long]] = {
  //    logTimeF(s"insertMessage(message = $message)") {
  //      db.run((Message returning Message.map(_.id)) += message)
  //        .map(Right(_))
  //    }
  //      .recover {
  //        case NonFatal(e) =>
  //          _logger.error(e.getMessage)
  //          Left(e)
  //      }
  //  }
  //
  //  def updateMessage(message: MessageRow)(implicit executor: ExecutionContext): Future[Either[Throwable, Long]] = {
  //    logTimeF(s"updateMessage(message = $message)") {
  //      db.run(
  //        Message
  //          .filter(_.id === message.id)
  //          .update(message)
  //      )
  //        .map(f =>
  //          if (f == 1) Right(message.id)
  //          else Left(new Throwable(s"Can't update message [id=${
  //            message.id
  //          }]"))
  //        )
  //    }
  //      .recover {
  //        case NonFatal(e) =>
  //          _logger.error(e.getMessage)
  //          Left(e)
  //      }
  //  }

  //  def deleteMessageFromDB(id: Long)(implicit executor: ExecutionContext): Future[Either[Throwable, Long]] = {
  //    logTimeF(s"deleteMessage(id = $id)") {
  //      db.run(
  //        Message
  //          .filter(_.id === id)
  //          .delete
  //      )
  //        .map(f =>
  //          if (f == 1) Right(id)
  //          else Left(new Throwable(s"Can't delete $id"))
  //        )
  //    }
  //      .recover {
  //        case NonFatal(e) => {
  //    _logger.error(e.getMessage)
  //  Left (e)
  //    }
  //      }
  //  }


  def insertOrUpdateUser(user: UserRow)(implicit executor: ExecutionContext): Future[Either[Throwable, Long]] = {
    logTimeF(s"insertOrUpdateUser(user = $user)") {
      db.run((User returning User.map(_.id)).insertOrUpdate(user))
        .map(f => Right(f.getOrElse(user.id)))
    }
      .recover {
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)
      }
  }

}
