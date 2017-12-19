package db

import java.util.Date

import com.anymessenger.db.AnymessengerDBObjects
import com.anymessenger.db.projection.{MessageRow, UserRow}
import org.slf4j.{Logger, LoggerFactory}
import util.logTime

import scala.concurrent.{Await, ExecutionContext, Future}

object Logic
  extends DBConfig
    with AnymessengerDBObjects {

  private implicit val logger: Logger = LoggerFactory.getLogger(this.getClass)

  import profile.api._

  def getUser(userId: Long)(implicit executor: ExecutionContext): Future[Either[String, Option[UserRow]]] =
    logTime(s"getUser(customerId = $userId)") {
      db.run(Users.filter(user =>
        user.id === userId &&
          !user.isDeleted &&
          user.isActive
      ).result)
        .map { res => Right(res.headOption)
        }
    }
      .recover {
        case e: Throwable =>
          Left(e.getMessage)
      }

  def getMessage(messageId: Long)(implicit executor: ExecutionContext): Future[Either[String, Option[MessageRow]]] =
    logTime(s"getMessage(messageId = $messageId)") {
      db.run(Messages.filter(message =>
        message.id === messageId &&
          !message.isDeleted
      ).result)
        .map { res => Right(res.headOption) }
    }
      .recover {
        case e: Throwable =>
          Left(e.getMessage)
      }


  def getUserMessages(userId: Long)(implicit executor: ExecutionContext): Future[Either[String, List[MessageRow]]] =
    logTime(s"getUserMessages(userId = $userId)") {
      db.run(Messages.filter(message =>
        message.userId === userId &&
          !message.isDeleted
      ).result)
        .map { res => Right(res.toList) }
    }
      .recover {
        case e: Throwable =>
          Left(e.getMessage)
      }


  def h2Init(): Unit = {

    val schema = Users.schema ++ Messages.schema
    Await.result(db.run(DBIO.seq(schema.create)), testFutureWaitTimeout)

    val addUser =
    (Users returning Users.map(_.id)) += UserRow(
      id = None,
      firstName = Some("TestFN_1"),
      lastName = Some("TestLN_1"),
      login = Some("TestLogin_1"),
      email = Some("TestEMAIL_1"),
      gender = Some(true), // false - fm , true - m
      description = None,
      isActive = true,
      createdAt = Some(new Date()),
      updatedAt = None,
      deletedAt = None,
      isDeleted = false
    )
    val userId = Await.result(db.run(addUser), testFutureWaitTimeout)

    val addMessages =
      Messages ++= Seq(
        MessageRow(
          id = None,
          text = Some("Test_text_1"),
          userId = userId,
          createdAt = Some(new Date()),
          updatedAt = None,
          deletedAt = None,
          isDeleted = false
        ),
        MessageRow(
          id = None,
          text = Some("Test_text_2"),
          userId = userId,
          createdAt = Some(new Date()),
          updatedAt = None,
          deletedAt = None,
          isDeleted = false
        )
      )
    db.run(addMessages)
  }

  def h2drop() {
    val schema = Users.schema ++ Messages.schema
    db.run(DBIO.seq(schema.drop))
  }

  // test
  def getOrCreateUserAndMessages(): Unit = {
    val res: Seq[UserRow] = Await.result(db.run(Users.result), testFutureWaitTimeout)
    if (res.isEmpty) {
      val addUser =
        (Users returning Users.map(_.id)) += UserRow(
          id = None,
          firstName = Some("TestFN_1"),
          lastName = Some("TestLN_1"),
          login = Some("TestLogin_1"),
          email = Some("TestEMAIL_1"),
          gender = Some(true), // false - fm , true - m
          description = None,
          isActive = true,
          createdAt = Some(new Date()),
          updatedAt = None,
          deletedAt = None,
          isDeleted = false
        )
      val userId = Await.result(db.run(addUser), testFutureWaitTimeout)
      userId.foreach { id =>
        logger.debug(s"userId: $id")
      }

      val addMessages =
        Messages ++= Seq(
          MessageRow(
            id = None,
            text = Some("Test_text_1"),
            userId = userId,
            createdAt = Some(new Date()),
            updatedAt = None,
            deletedAt = None,
            isDeleted = false
          ),
          MessageRow(
            id = None,
            text = Some("Test_text_2"),
            userId = userId,
            createdAt = Some(new Date()),
            updatedAt = None,
            deletedAt = None,
            isDeleted = false
          )
        )
      Await.result(db.run(addMessages), testFutureWaitTimeout)


      val res2 = Await.result(db.run(Users.result), testFutureWaitTimeout)
      res2.foreach { v =>
        logger.info(v.toString)
      }
    } else {
      res.foreach { v =>
        logger.info(v.toString)
      }
    }

  }
}
