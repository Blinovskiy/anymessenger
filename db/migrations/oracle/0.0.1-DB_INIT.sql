CREATE TABLE "user" (
  "id"          NUMBER                 NOT NULL,
  "firstname"   VARCHAR2(32),
  "lastname"    VARCHAR2(32),
  "login"       VARCHAR2(32)           NOT NULL,
  "email"       VARCHAR2(32)           NOT NULL,
  "gender"      NUMBER(1),
  "description" VARCHAR2(256),
  "createdat"   TIMESTAMP(6) DEFAULT SYSDATE,
  "updatedat"   TIMESTAMP(6),
  "isActive"    NUMBER(1) DEFAULT 1    NOT NULL,
  "deletedat"   TIMESTAMP(6),
  "isdeleted"   NUMBER(1) DEFAULT 0    NOT NULL,
  CONSTRAINT "userpk" PRIMARY KEY ("id")
);
CREATE SEQUENCE "usersequence" START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER "usertrigger"
BEFORE INSERT ON "user"
FOR EACH ROW WHEN (new."id" IS NULL)
  BEGIN SELECT "usersequence".nextval
        INTO :new."id"
        FROM DUAL;
  END;

CREATE TABLE "message" (
  "id"        NUMBER              NOT NULL,
  "text"      VARCHAR2(256),
  "userid"    NUMBER,
  "createdat" TIMESTAMP(6),
  "updatedat" TIMESTAMP(6),
  "deletedat" TIMESTAMP(6),
  "isdeleted" NUMBER(1) DEFAULT 0 NOT NULL,
  CONSTRAINT "messagepk" PRIMARY KEY ("id")
);
CREATE SEQUENCE "messagesequence" START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER "messagetrigger"
BEFORE INSERT ON "message"
FOR EACH ROW WHEN (new."id" IS NULL)
  BEGIN SELECT "messagesequence".nextval
        INTO :new."id"
        FROM DUAL;
  END;

BEGIN
  INSERT INTO "db_version" ("db_version", "description") VALUES ('0.0.1', 'Database init');

  COMMIT;

  DBMS_OUTPUT.PUT_LINE('script completed without errors');
  -- exception block, if something bad happens it will end here
  EXCEPTION
  -- WE ARE NOT INTERESTED IN A SPECIFIC ERROR, SO A WHEN OTHERS WILL HANDLE OUR CASE
  WHEN OTHERS THEN
  -- print the error
  DBMS_OUTPUT.PUT_LINE('error on script: error number: ' || SQLCODE || ', error message: ' || SQLERRM);
  DBMS_OUTPUT.PUT_LINE(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
  ROLLBACK;
END;
/
EXIT;