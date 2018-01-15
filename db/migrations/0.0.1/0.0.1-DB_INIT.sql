create table "userinfo" (
  id          bigserial,
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

create table "message" (
  id        bigserial                not null,
  text      varchar(256),
  userid    bigint,
  createdat timestamp(6),
  updatedat timestamp(6),
  deletedat timestamp(6),
  isdeleted boolean default false not null
);