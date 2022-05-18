--liquibase formatted sql
--changeset resource_management:1
create or replace view task_type_statistics as
select tt.id,
       stddev((t.fact_time - tt.constant_bias) / t.square) as sigma,
       avg((t.fact_time - tt.constant_bias) / t.square)       e,
       constant_bias,
       man_hour_per_square_meter
from task t
         inner join task_type tt on t.type_id = tt.id
group by tt.id;

--changeset resource_management:2
alter table if exists task add column if not exists abnormal boolean default false not null;

--changeset resource_management:3
alter table if exists task rename column square to unit_value;

alter table task_type
    rename column man_hour_per_square_meter to man_hour_per_unit;

alter table task_type
    rename column man_hour_per_square_meter_draft to man_hour_per_unit_draft;

drop table if exists app_user_teams;

--changeset resource_management:4
drop view task_type_statistics;

create or replace view task_type_statistics as
select tt.id,
       stddev((t.fact_time - tt.constant_bias) / t.unit_value) as sigma,
       avg((t.fact_time - tt.constant_bias) / t.unit_value)       e,
       constant_bias,
       man_hour_per_unit
from task t
         inner join task_type tt on t.type_id = tt.id
group by tt.id;

--changeset resource_management:5

create sequence grade_sec;

create table if not exists grade(
    id bigint primary key,
    name varchar(100),
    coefficient double precision not null default 1.0
);

insert into grade(id, name) values (nextval('grade_sec'), 'Инженер 1-ой категории');