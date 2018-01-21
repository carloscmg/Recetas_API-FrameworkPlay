# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table api_key (
  id                            bigint auto_increment not null,
  key                           varchar(255),
  version                       bigint not null,
  when_created                  timestamp not null,
  when_updated                  timestamp not null,
  constraint pk_api_key primary key (id)
);

create table cocinero (
  id                            bigint auto_increment not null,
  nombre                        varchar(255),
  apellidos                     varchar(255),
  pais                          varchar(255),
  ficha_contacto_id             bigint,
  version                       bigint not null,
  when_created                  timestamp not null,
  when_updated                  timestamp not null,
  constraint uq_cocinero_ficha_contacto_id unique (ficha_contacto_id),
  constraint pk_cocinero primary key (id)
);

create table etiqueta (
  id                            bigint auto_increment not null,
  nombre                        varchar(255),
  version                       bigint not null,
  when_created                  timestamp not null,
  when_updated                  timestamp not null,
  constraint pk_etiqueta primary key (id)
);

create table ingrediente (
  id                            bigint auto_increment not null,
  nombre                        varchar(255),
  cantidad                      varchar(255),
  receta_id                     bigint,
  version                       bigint not null,
  when_created                  timestamp not null,
  when_updated                  timestamp not null,
  constraint pk_ingrediente primary key (id)
);

create table receta (
  id                            bigint auto_increment not null,
  titulo                        varchar(255),
  tipo_cocina                   varchar(255),
  dificultad                    varchar(255),
  tiempo                        integer,
  elaboracion                   varchar(255),
  numero_personas               integer,
  imagen                        varchar(255),
  cocinero_id                   bigint,
  version                       bigint not null,
  when_created                  timestamp not null,
  when_updated                  timestamp not null,
  constraint pk_receta primary key (id)
);

create table receta_etiqueta (
  receta_id                     bigint not null,
  etiqueta_id                   bigint not null,
  constraint pk_receta_etiqueta primary key (receta_id,etiqueta_id)
);

create table ficha_contacto (
  id                            bigint auto_increment not null,
  telefono                      integer,
  email                         varchar(255),
  web                           varchar(255),
  version                       bigint not null,
  when_created                  timestamp not null,
  when_updated                  timestamp not null,
  constraint pk_ficha_contacto primary key (id)
);

alter table cocinero add constraint fk_cocinero_ficha_contacto_id foreign key (ficha_contacto_id) references ficha_contacto (id) on delete restrict on update restrict;

alter table ingrediente add constraint fk_ingrediente_receta_id foreign key (receta_id) references receta (id) on delete restrict on update restrict;
create index ix_ingrediente_receta_id on ingrediente (receta_id);

alter table receta add constraint fk_receta_cocinero_id foreign key (cocinero_id) references cocinero (id) on delete restrict on update restrict;
create index ix_receta_cocinero_id on receta (cocinero_id);

alter table receta_etiqueta add constraint fk_receta_etiqueta_receta foreign key (receta_id) references receta (id) on delete restrict on update restrict;
create index ix_receta_etiqueta_receta on receta_etiqueta (receta_id);

alter table receta_etiqueta add constraint fk_receta_etiqueta_etiqueta foreign key (etiqueta_id) references etiqueta (id) on delete restrict on update restrict;
create index ix_receta_etiqueta_etiqueta on receta_etiqueta (etiqueta_id);


# --- !Downs

alter table cocinero drop constraint if exists fk_cocinero_ficha_contacto_id;

alter table ingrediente drop constraint if exists fk_ingrediente_receta_id;
drop index if exists ix_ingrediente_receta_id;

alter table receta drop constraint if exists fk_receta_cocinero_id;
drop index if exists ix_receta_cocinero_id;

alter table receta_etiqueta drop constraint if exists fk_receta_etiqueta_receta;
drop index if exists ix_receta_etiqueta_receta;

alter table receta_etiqueta drop constraint if exists fk_receta_etiqueta_etiqueta;
drop index if exists ix_receta_etiqueta_etiqueta;

drop table if exists api_key;

drop table if exists cocinero;

drop table if exists etiqueta;

drop table if exists ingrediente;

drop table if exists receta;

drop table if exists receta_etiqueta;

drop table if exists ficha_contacto;

