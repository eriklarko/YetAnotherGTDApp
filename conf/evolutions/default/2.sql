# --- !Ups

create table user (
  id                        bigint not null,
  email                     varchar(255),
  password                  varchar(255),
  constraint pk_user primary key (id))
;
insert into user (id, email, password) values (1, 'hej@hej.hej', 'lol');


alter table filter add column owner_id bigint not null default 1;
alter table filter add column starred boolean default false;

alter table note add column owner_id bigint not null default 1;

alter table tag add column owner_id bigint not null default 1;



create sequence user_seq;



alter table filter add constraint fk_filter_owner_1 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_filter_owner_1 on filter (owner_id);
alter table note add constraint fk_note_owner_2 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_note_owner_2 on note (owner_id);
alter table tag add constraint fk_tag_owner_3 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_tag_owner_3 on tag (owner_id);


# --- !Downs

drop index if exists ix_filter_owner_1;
drop index if exists ix_note_owner_2;
drop index if exists ix_tag_owner_3;

alter table filter drop column owner_id;
alter table filter drop column starred;
alter table note drop column owner_id;
alter table tag drop column owner_id;

drop table if exists user;
drop sequence if exists user_seq;


