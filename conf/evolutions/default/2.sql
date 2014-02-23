create table user (
  id                        bigint not null,
  email                     varchar(255),
  password                  varchar(255),
  constraint pk_user primary key (id))
;



alter table filter add column owner_id bigint not null,
alter table filter add column starred boolean,

alter table note add column owner_id bigint not null,

alter table tag add column owner_id bigint not null,



create sequence user_seq;



alter table filter add constraint fk_filter_owner_1 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_filter_owner_1 on filter (owner_id);
alter table note add constraint fk_note_owner_2 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_note_owner_2 on note (owner_id);
alter table tag add constraint fk_tag_owner_3 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_tag_owner_3 on tag (owner_id);
