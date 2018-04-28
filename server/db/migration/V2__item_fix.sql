drop table if exists t_item;

create table t_item (
    id bigint not null primary key,
    name character varying(250) not null unique,

    mesh_file character varying(250),
    mesh_file_drop character varying(250),
    material character varying(250),
    weight double precision
);

alter table t_item owner to zombie;
