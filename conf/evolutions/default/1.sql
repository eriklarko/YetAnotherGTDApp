# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table tag (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_tag primary key (id))
;

create table taggable_object (
  id                        bigint not null,
  payload                   varchar(255),
  constraint pk_taggable_object primary key (id))
;


create table taggable_object_tag (
  taggable_object_id             bigint not null,
  tag_id                         bigint not null,
  constraint pk_taggable_object_tag primary key (taggable_object_id, tag_id))
;
create sequence tag_seq;

create sequence taggable_object_seq;




alter table taggable_object_tag add constraint fk_taggable_object_tag_taggab_01 foreign key (taggable_object_id) references taggable_object (id) on delete restrict on update restrict;

alter table taggable_object_tag add constraint fk_taggable_object_tag_tag_02 foreign key (tag_id) references tag (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists tag;

drop table if exists taggable_object;

drop table if exists taggable_object_tag;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists tag_seq;

drop sequence if exists taggable_object_seq;

