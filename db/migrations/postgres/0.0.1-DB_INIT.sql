-- id: Long,
-- firstName: Option[String],
-- lastName: Option[String],
-- login: String,
-- email: String,
-- gender: Option[Long],
-- description: Option[String],
-- isActive: Boolean = true,
-- createdAt: Option[Date],
-- updatedAt: Option[Date],
-- deletedAt: Option[Date],
-- isDeleted: Boolean = false
create table "user" (
  id          serial,
  firstname   varchar(32),
  lastname    varchar(32),
  login       varchar(32)              not null,
  email       varchar(32)              not null,
  gender      boolean,
  description varchar(256),
  createdat   timestamp(6) default now(),
  updatedat   timestamp(6),
  isactive    boolean default true     not null,
  deletedat   timestamp(6),
  isdeleted   boolean default false    not null
);

-- id: long,
-- text: option[string],
-- userid: option[long],
-- createdat: option[date],
-- updatedat: option[date],
-- isdeleted: boolean = false

create table "message" (
  id        serial                not null,
  text      varchar(256),
  userid    integer,
  createdat timestamp(6),
  updatedat timestamp(6),
  deletedat timestamp(6),
  isdeleted boolean default false not null
);