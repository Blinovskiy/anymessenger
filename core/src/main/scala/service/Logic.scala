package service

import java.util.Date

import com.anymessenger.db.AnymessengerDBObjects
import com.anymessenger.db.projection.UserRow
import db.DBConfig
import org.slf4j.{Logger, LoggerFactory}
import util.logTime

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

object Logic
  extends DBConfig
    with AnymessengerDBObjects {

  private implicit val logger: Logger = LoggerFactory.getLogger(Logic.getClass)

  import profile.api._

  def getUser(userId: Long)(implicit executor: ExecutionContext): Future[Either[String, Option[UserRow]]] =
    logTime(s"getUser(customerId = $userId)") {
      db.run(Users.filter(user =>
        user.id === userId &&
          !user.isDeleted &&
          user.isActive
      ).result)
        .map {
          res =>
            Right(
              res.headOption
            )
        }
    }
      .recover {
        case e: Throwable =>
          Left(e.getMessage)
      }

  def test(): Unit = {
    val res: Seq[UserRow] = Await.result(db.run(Users.result), 5 second)
    if (res.isEmpty) {
      val addUser =
        (Users returning Users.map(_.id)) += UserRow(
          id = None,
          firstName = Some("TestFN"),
          lastName = Some("TestLN"),
          login = Some("TestLogin"),
          email = Some("TestEMAIL"),
          gender = Some(true), // false - fm , true - m
          description = None,
          isActive = true,
          createdAt = Some(new Date()),
          updatedAt = None,
          deletedAt = None,
          isDeleted = false
        )
      val userId = Await.result(db.run(addUser), 5 second)
      userId.foreach { id =>
        logger.debug(s"userId: $id")
      }

      val res2 = Await.result(db.run(Users.result), 5 second)
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
