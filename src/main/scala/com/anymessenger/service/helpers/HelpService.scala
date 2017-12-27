package com.anymessenger.service.helpers

import java.sql.Timestamp
import java.util.Date

import com.anymessenger.model.Tables._
import db.DBConfig
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Await

object HelpService extends DBConfig {

  private implicit val logger: Logger = LoggerFactory.getLogger(this.getClass)

  import profile.api._

  def h2Init(): Unit = {

    val schema = User.schema ++ Message.schema
    Await.result(db.run(DBIO.seq(schema.create)), testFutureWaitTimeout)

    val addUser =
      (User returning User.map(_.id)) += UserRow(
        id = None,
        firstname = Some("TestFN_1"),
        lastname = Some("TestLN_1"),
        login = Some("TestLogin_1"),
        email = Some("TestEMAIL_1"),
        gender = Some(true), // false - fm , true - m
        description = None,
        isactive = true,
        createdat = Some(new Timestamp(new Date().getTime)),
        updatedat = None,
        deletedat = None,
        isdeleted = false
      )

    val userid = Await.result(db.run(addUser), testFutureWaitTimeout)

    val addMessage =
      Message ++= Seq(
        MessageRow(
          id = None,
          text = Some("Test_text_1"),
          userid = userid,
          //          createdat = Some(new Date()),
          createdat = Some(new Timestamp(new Date().getTime)),
          updatedat = None,
          deletedat = None,
          isdeleted = false
        ),
        MessageRow(
          id = None,
          text = Some("Test_text_2"),
          userid = userid,
          //          createdat = Some(new Date()),
          createdat = Some(new Timestamp(new Date().getTime)),
          updatedat = None,
          deletedat = None,
          isdeleted = false
        )
      )
    db.run(addMessage)
  }

  def h2drop() {
    val schema = User.schema ++ Message.schema
    db.run(DBIO.seq(schema.drop))
  }

  // test
  def getOrCreateUserAndMessages(): Unit = {
    val res: Seq[UserRow] = Await.result(db.run(User.result), testFutureWaitTimeout)
    if (res.isEmpty) {
      val addUser =
        (User returning User.map(_.id)) += UserRow(
          id = None,
          firstname = Some("TestFN_1"),
          lastname = Some("TestLN_1"),
          login = Some("TestLogin_1"),
          email = Some("TestEMAIL_1"),
          gender = Some(true), // false - fm , true - m
          description = None,
          isactive = true,
          //          createdat = Some(new Date()),
          createdat = Some(new Timestamp(new Date().getTime)),
          updatedat = None,
          deletedat = None,
          isdeleted = false
        )
      val userid = Await.result(db.run(addUser), testFutureWaitTimeout)
      logger.debug(s"userid: $userid")

      val addMessage =
        Message ++= Seq(
          MessageRow(
            id = None,
            text = Some("Test_text_1"),
            userid = userid,
            //            createdat = Some(new Date()),
            createdat = Some(new Timestamp(new Date().getTime)),
            updatedat = None,
            deletedat = None,
            isdeleted = false
          ),
          MessageRow(
            id = None,
            text = Some("Test_text_2"),
            userid = userid,
            //            createdat = Some(new Date()),
            createdat = Some(new Timestamp(new Date().getTime)),
            updatedat = None,
            deletedat = None,
            isdeleted = false
          )
        )
      Await.result(db.run(addMessage), testFutureWaitTimeout)


      val res2 = Await.result(db.run(User.result), testFutureWaitTimeout)
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