drop table IF EXISTS categories CASCADE;
drop table IF EXISTS users CASCADE;
drop table IF EXISTS locations CASCADE;
drop table IF EXISTS events CASCADE;
drop table IF EXISTS requests CASCADE;
drop table IF EXISTS compilation CASCADE;
drop table IF EXISTS eventc_compilations CASCADE;


create table if not exists categories (
  id bigint GENERATED always AS IDENTITY not null,
  name varchar(50) not null,
  constraint pk_categories primary key (id),
  constraint uq_category_name unique (name)
);

create table if not exists users (
  id bigint GENERATED always AS IDENTITY not null,
  email varchar(254) not null,
  name varchar(250) not null,
  constraint pk_users primary key (id),
  constraint uq_user_email unique (email)
);

create table if not exists locations (
  id bigint GENERATED always AS IDENTITY not null,
  lat float not null,
  lon float not null,
  constraint pk_locations primary key (id)
);

create table if not exists events (
  id bigint GENERATED always AS IDENTITY not null,
  annotation varchar(2000) not null,
  category_id int not null,
  description varchar(7000) not null,
  event_date timestamp not null,
  location_id int not null,
  paid boolean,
  participantLimit int,
  requestModeration boolean,
  title varchar(120) not null,

  constraint pk_events primary key (id),
  constraint fk_events_to_categories foreign key(category_id) references categories(id),
  constraint fk_events_to_locations foreign key(location_id) references locations(id)
);

create table if not exists requests (
  id bigint GENERATED always AS IDENTITY not null,
  created timestamp,
  event_id int,
  requester_id int,
  status varchar,
  constraint pk_requests primary key (id),
  constraint fk_requests_to_events foreign key(event_id) references events(id),
  constraint fk_requests_to_users foreign key(requester_id) references users(id)
);


create table if not exists compilations (
  id bigint GENERATED always AS IDENTITY not null,
  pinned boolean,
  title varchar(50) not null,
  constraint pk_users primary key (id)
);

create table if not exists events_compilations (
  event_id int not null,
  compilation_id int not null,
  constraint fk_ec_to_events foreign key(event_id) references events(id),
  constraint fk_ec_to_compilations foreign key(compilation_id) references compilations(id)
);

