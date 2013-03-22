-- Alter Invoice table
alter table mir15invoice add column mir15date DATETIME NULL;

alter table mir15invoice change mir15date mir15orderdate DATETIME NULL;

alter table mir15invoice add column mir15finishdate DATETIME NULL;