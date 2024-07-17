drop table IF EXISTS stats CASCADE;

create table if not exists stats (
  id bigint GENERATED always AS IDENTITY not null,
  app varchar(255) not null,
  uri varchar(255) not null,
  ip varchar(255) not null,
  times_tamp timestamp not null,
  constraint pk_stats primary key (id)
);