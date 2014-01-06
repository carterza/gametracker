# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table game (
  id                        bigint not null,
  title                     varchar(255),
  owned                     boolean,
  constraint pk_game primary key (id))
;

create table account (
  id                        bigint not null,
  email                     varchar(255),
  name                      varchar(255),
  password                  varchar(255),
  constraint pk_account primary key (id))
;

create table vote (
  id                        bigint not null,
  voter_id                  bigint,
  game_id                   bigint,
  constraint pk_vote primary key (id))
;

create sequence game_seq;

create sequence account_seq;

create sequence vote_seq;

alter table vote add constraint fk_vote_voter_1 foreign key (voter_id) references account (id) on delete restrict on update restrict;
create index ix_vote_voter_1 on vote (voter_id);
alter table vote add constraint fk_vote_game_2 foreign key (game_id) references game (id) on delete restrict on update restrict;
create index ix_vote_game_2 on vote (game_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists game;

drop table if exists account;

drop table if exists vote;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists game_seq;

drop sequence if exists account_seq;

drop sequence if exists vote_seq;

