# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table filter (
  id                        bigint not null,
  owner_id                  bigint not null,
  name                      varchar(255),
  search_tree               clob,
  starred                   boolean,
  constraint pk_filter primary key (id))
;

create table note (
  id                        bigint not null,
  owner_id                  bigint not null,
  payload                   clob not null,
  constraint pk_note primary key (id))
;

create table tag (
  id                        bigint not null,
  owner_id                  bigint not null,
  name                      clob,
  constraint pk_tag primary key (id))
;

create table user (
  id                        bigint not null,
  email                     varchar(255),
  password                  varchar(255),
  constraint pk_user primary key (id))
;


create table note_tag (
  note_id                        bigint not null,
  tag_id                         bigint not null,
  constraint pk_note_tag primary key (note_id, tag_id))
;
create sequence filter_seq;

create sequence note_seq;

create sequence tag_seq;

create sequence user_seq;

alter table filter add constraint fk_filter_owner_1 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_filter_owner_1 on filter (owner_id);
alter table note add constraint fk_note_owner_2 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_note_owner_2 on note (owner_id);
alter table tag add constraint fk_tag_owner_3 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_tag_owner_3 on tag (owner_id);



alter table note_tag add constraint fk_note_tag_note_01 foreign key (note_id) references note (id) on delete restrict on update restrict;

alter table note_tag add constraint fk_note_tag_tag_02 foreign key (tag_id) references tag (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists filter;

drop table if exists note;

drop table if exists note_tag;

drop table if exists tag;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists filter_seq;

drop sequence if exists note_seq;

drop sequence if exists tag_seq;

drop sequence if exists user_seq;

