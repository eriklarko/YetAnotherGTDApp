# --- !Ups

create table filter (
  id                        bigint not null,
  name                      varchar(255),
  search_tree               clob,
  constraint pk_filter primary key (id))
;

create table note (
  id                        bigint not null,
  payload                   clob not null,
  constraint pk_note primary key (id))
;

create table tag (
  id                        bigint not null,
  name                      clob,
  constraint pk_tag primary key (id))
;


create table note_tag (
  note_id                        bigint not null,
  tag_id                         bigint not null,
  constraint pk_note_tag primary key (note_id, tag_id))
;
create sequence filter_seq;

create sequence note_seq;

create sequence tag_seq;




alter table note_tag add constraint fk_note_tag_note_01 foreign key (note_id) references note (id) on delete restrict on update restrict;

alter table note_tag add constraint fk_note_tag_tag_02 foreign key (tag_id) references tag (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists filter;

drop table if exists note;

drop table if exists note_tag;

drop table if exists tag;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists filter_seq;

drop sequence if exists note_seq;

drop sequence if exists tag_seq;

