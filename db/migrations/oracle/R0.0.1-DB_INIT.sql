DROP TABLE "MESSAGE";
DROP SEQUENCE MESSAGESEQUENCE;
DROP TABLE "USER";
DROP SEQUENCE USERSEQUENCE;

BEGIN
  delete DB_VERSION where DB_VERSION ='0.0.1' and  DESCRIPTION='Database init';

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