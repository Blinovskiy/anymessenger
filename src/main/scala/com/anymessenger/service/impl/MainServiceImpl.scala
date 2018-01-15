package com.anymessenger.service.impl

import java.sql.Timestamp

import com.anymessenger.config.Slicker
import com.anymessenger.dbmodel.Tables._
import com.anymessenger.dbmodel.Tables.profile.api._
import com.anymessenger.service.api.{ReadingServices, WritingServices}
import com.anymessenger.service.model.request.{RequestMessageEntity, RequestUserEntity}
import com.anymessenger.service.model.response.{ResponseMessageEntity, ResponseUserEntity}
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

  def getUser(userid: Long)(implicit executor: ExecutionContext): Future[Either[Throwable, Option[ResponseUserEntity]]] =
    logTimeF(s"getUser(userid = $userid)") {
      db.run(Userinfo.filter(user =>
        user.id === userid &&
          !user.isdeleted &&
          user.isactive
      ).result)
        .map { seq =>
          Right(seq.headOption.map(res =>
            ResponseUserEntity(
              res.id,
              res.firstname,
              res.lastname,
              res.login,
              res.email,
              res.gender,
              res.description,
              res.isactive,
              res.createdat,
              res.updatedat
            )))
        }
    }
      .recover {
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)
      }

  def getUserMessages(userid: Long)(implicit executor: ExecutionContext): Future[Either[Throwable, List[ResponseMessageEntity]]] =
    logTimeF(s"getUserMessage(userid = $userid)") {
      db.run(Message
        .filter(message => message.userid === userid && !message.isdeleted)
        .join(Userinfo.filter(!_.isdeleted))
        .on(_.userid === _.id)
        .sortBy(_._1.createdat)
        .result)
        .map { f =>
          Right(
            f.map(res =>
              ResponseMessageEntity(
                res._1.id,
                res._1.text,
                ResponseUserEntity(
                  res._2.id,
                  res._2.firstname,
                  res._2.lastname,
                  res._2.login,
                  res._2.email,
                  res._2.gender,
                  res._2.description,
                  res._2.isactive,
                  res._2.createdat,
                  res._2.updatedat
                ),
                res._1.createdat,
                res._1.updatedat
              )
            ).toList
          )
        }
    }
      .recover {
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)
      }

  def getAllMessages()(implicit executor: ExecutionContext): Future[Either[Throwable, List[ResponseMessageEntity]]] =
    logTimeF(s"getAllMessages()") {
      db.run(Message
        .filter(!_.isdeleted)
        .join(Userinfo.filter(!_.isdeleted))
        .on(_.userid === _.id)
        .sortBy(_._1.createdat)
        .result)
        .map { f =>
          Right(
            f.map(res =>
              ResponseMessageEntity(
                res._1.id,
                res._1.text,
                ResponseUserEntity(
                  res._2.id,
                  res._2.firstname,
                  res._2.lastname,
                  res._2.login,
                  res._2.email,
                  res._2.gender,
                  res._2.description,
                  res._2.isactive,
                  res._2.createdat,
                  res._2.updatedat
                ),
                res._1.createdat,
                res._1.updatedat
              )
            ).toList
          )
        }
    }
      .recover {
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)
      }


  def getMessagesByDate(from: Timestamp, to: Timestamp)(implicit executor: ExecutionContext): Future[Either[Throwable, List[ResponseMessageEntity]]] =
    logTimeF(s"getMessagesByDate(from = $from, to = $to)") {
      db.run(Message
        .filter(f => !f.isdeleted && f.createdat >= from && f.createdat <= to)
        .join(Userinfo.filter(!_.isdeleted))
        .on(_.userid === _.id)
        .sortBy(_._1.createdat)
        .result)
        .map { f =>
          Right(
            f.map(res =>
              ResponseMessageEntity(
                res._1.id,
                res._1.text,
                ResponseUserEntity(
                  res._2.id,
                  res._2.firstname,
                  res._2.lastname,
                  res._2.login,
                  res._2.email,
                  res._2.gender,
                  res._2.description,
                  res._2.isactive,
                  res._2.createdat,
                  res._2.updatedat
                ),
                res._1.createdat,
                res._1.updatedat
              )
            ).toList
          )
        }
    }
      .recover {
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)
      }


  def getLastNMessages(n: Int)(implicit executor: ExecutionContext): Future[Either[Throwable, List[ResponseMessageEntity]]] =
    logTimeF(s"getLastNMessages(n = $n)") {
      db.run(Message
        .filter(f => !f.isdeleted)
        .sortBy(_.createdat.desc)
        .take(n)
        .join(Userinfo.filter(!_.isdeleted))
        .on(_.userid === _.id)
        .sortBy(_._1.createdat.asc)
        .result)
        .map { f =>
          Right(
            f.map(res =>
              ResponseMessageEntity(
                res._1.id,
                res._1.text,
                ResponseUserEntity(
                  res._2.id,
                  res._2.firstname,
                  res._2.lastname,
                  res._2.login,
                  res._2.email,
                  res._2.gender,
                  res._2.description,
                  res._2.isactive,
                  res._2.createdat,
                  res._2.updatedat
                ),
                res._1.createdat,
                res._1.updatedat
              )
            ).toList
          )
        }
    }
      .recover {
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)
      }


  def getMessage(messageId: Long)(implicit executor: ExecutionContext): Future[Either[Throwable, Option[ResponseMessageEntity]]] =
    logTimeF(s"getMessage(messageId = $messageId)") {
      db.run(
        Message.filter(message =>
          message.id === messageId &&
            !message.isdeleted
        )
          .join(Userinfo.filter(!_.isdeleted))
          .on(_.userid === _.id)

          .result)
        .map { f =>
          Right(
            f.headOption.map(res =>
              ResponseMessageEntity(
                res._1.id,
                res._1.text,
                ResponseUserEntity(
                  res._2.id,
                  res._2.firstname,
                  res._2.lastname,
                  res._2.login,
                  res._2.email,
                  res._2.gender,
                  res._2.description,
                  res._2.isactive,
                  res._2.createdat,
                  res._2.updatedat
                ),
                res._1.createdat,
                res._1.updatedat
              )
            )
          )
        }
    }
      .recover {
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)

      }


  def insertOrUpdateMessage(messageEntity: RequestMessageEntity)(implicit executor: ExecutionContext): Future[Either[Throwable, Long]] = {
    logTimeF(s"insertOrUpdateMessage(messageEntity = $messageEntity)") {
      messageEntity match {
        // insert
        case RequestMessageEntity(None | Some(-1L), text, userId) =>
          db.run(
            Message.map(m => (m.text, m.userid, m.createdat)) returning Message.map(_.id) += (Some(text), Some(userId), Some(nowTimestamp))
          )
            .map(Right(_))

        // update
        case RequestMessageEntity(Some(id), text, userId) =>
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
    logTimeF(s"deleteMessage(id = $id)") {
      db.run(
        Message
          .filter(_.id === id)
          .map(m => (m.isdeleted, m.deletedat))
          .update((true, Some(nowTimestamp)))
      )
        .map(f =>
          if (f == 1) Right(id)
          else Left(new Throwable(s"Can't delete message [id=$id]"))
        )
    }
      .recover {
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)
      }
  }

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

  def insertOrUpdateUser(userEntity: RequestUserEntity)(implicit executor: ExecutionContext): Future[Either[Throwable, Long]] = {
    logTimeF(s"insertOrUpdateUser(userEntity = $userEntity)") {
      userEntity match {
        // insert
        case RequestUserEntity(None | Some(-1L), firstname, lastname, Some(login), Some(email), gender, description, isactive) =>
          db.run(
            Userinfo.map(u => (u.firstname, u.lastname, u.login, u.email, u.gender, u.description, u.createdat, u.isactive))
              returning Userinfo.map(_.id) += ((firstname, lastname, login, email, gender, description, Some(nowTimestamp), isactive))
          )
            .map(Right(_))

        // update
        case RequestUserEntity(Some(id), firstname, lastname, Some(login), Some(email), gender, description, isactive) =>
          db.run(
            Userinfo
              .filter(_.id === id)
              .map(u => (u.firstname, u.lastname, u.login, u.email, u.gender, u.description, u.updatedat, u.isactive))
              .update((firstname, lastname, login, email, gender, description, Some(nowTimestamp), isactive))
          )
            .map(f =>
              if (f == 1) Right(id)
              else Left(new Throwable(s"Can't update user [id=$id]"))
            )
      }
    }
      .recover {
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)
      }
  }

  def deleteUser(id: Long)(implicit executor: ExecutionContext): Future[Either[Throwable, Long]] = {
    logTimeF(s"deleteUser(id = $id)") {
      db.run(
        Userinfo
          .filter(_.id === id)
          .map(u => (u.isdeleted, u.deletedat))
          .update((true, Some(nowTimestamp)))
      )
        .map(f =>
          if (f == 1) Right(id)
          else Left(new Throwable(s"Can't delete user [id=$id]"))
        )
    }
      .recover {
        case NonFatal(e) =>
          _logger.error(e.getMessage)
          Left(e)
      }
  }

}
