drop table if exists t_item;

create table t_item (
    id bigint not null primary key,
    name character varying(250) not null unique,

    mesh_file character varying(250) not null,
    mesh_file_drop character varying(250) not null,
    material character varying(250) not null,
    weight double precision

    --     constraint t_item_name_u unique (name)
);

alter table t_item owner to zombie;