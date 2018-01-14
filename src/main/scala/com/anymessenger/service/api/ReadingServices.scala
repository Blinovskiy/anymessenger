package com.anymessenger.service.api

import java.sql.Timestamp

import com.anymessenger.dbmodel.Tables._

import scala.concurrent.{ExecutionContext, Future}

trait ReadingServices {

  def getUser(userid: Long)(implicit executor: ExecutionContext): Future[Either[Throwable, Option[UserRow]]]

  def getMessage(messageId: Long)(implicit executor: ExecutionContext): Future[Either[Throwable, Option[MessageRow]]]

  def getUserMessages(userid: Long)(implicit executor: ExecutionContext): Future[Either[Throwable, List[MessageRow]]]

  def getAllMessages()(implicit executor: ExecutionContext): Future[Either[Throwable, List[MessageRow]]]

  def getMessagesByDate(from: Timestamp, to: Timestamp)(implicit executor: ExecutionContext): Future[Either[Throwable, List[MessageRow]]]

  def getLastNMessages(n: Int)(implicit executor: ExecutionContext): Future[Either[Throwable, List[MessageRow]]]

}
