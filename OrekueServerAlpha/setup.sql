/* Auto Generated Sql File By VBA */
create table user_table(
  _id integer primary key autoincrement,
  device_id text unique not null,
  account_id text unique not null,
  hashed_password text not null,
  name text not null,
  icon text default null,
  title_id integer default null,
  prefix_id integer default null,
  param_study integer default 0,
  param_exercise integer default 0,
  param_communication integer default 0,
  param_fashion integer default 0,
  param_society integer default 0,
  param_art integer default 0,
  param_level integer default 0,
  );
create table title_table(
  _id integer primary key autoincrement,
  name text ,
  icon text );
create table prefix_table(
  _id integer primary key autoincrement,
  name text );
create table friends_tabel(
  _id integer primary key autoincrement,
  user_id1 integer not null,
  user_id2 integer not null);
create table released_title_table(
  _id integer primary key autoincrement,
  user_id integer not null,
  title_id integer not null);
create table released_prefix_table(
  _id integer primary key autoincrement,
  user_id integer not null,
  prefix_id integer not null);
create table activity_table(
  _id integer primary key autoincrement,
  content text ,
  user_id integer ,
  time_stamp integer ,
  duration integer ,
  date integer ,
  category_id integer ,
  tag_id integer ,
  std_inc integer default 0,
  exe_inc integer default 0,
  com_inc integer default 0,
  fas_inc integer default 0,
  soc_inc integer default 0,
  art_inc integer default 0);
create table cooperation_table(
  _id integer primary key autoincrement,
  activity_id1 integer not null,
  activity_id2 integer not null);
create table quest_table(
  _id integer primary key autoincrement,
  user_id integer not null,
  quest_id integer not null);
create table friend_table(
  _id integer primary key autoincrement,
  user1_id integer not null,
  user2_id integer not null,
  state integer not null);
