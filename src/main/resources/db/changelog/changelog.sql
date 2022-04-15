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
alter table if exists task add column if not exists abnormal boolean default false not null
