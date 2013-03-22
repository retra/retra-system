
create index i_mir11from on mir11schedule (mir11from);
create index i_mir11to on mir11schedule (mir11to);
create index i_mir11fromTo on mir11schedule (mir11from, mir11to);
create index i_mir11employeeFromTo on mir11schedule (sis10pk, mir11from, mir11to);

create index i_mir10from on mir10worklog (mir10from);
create index i_mir10to on mir10worklog (mir10to);
create index i_mir10fromTo on mir10worklog (mir10from, mir10to);
create index i_mir10employeeFromTo on mir10worklog (sis10pk, mir10from, mir10to);

create view mirv01worklogoverview as select sis10pk, DATE(mir10from) as mirv01date, mir01pk, mir03pk,sum((unix_timestamp(mir10to)-unix_timestamp(mir10from))/(60*60)) as mirv01hours from mir10worklog group by sis10pk, mir01pk, mir03pk, mirv01date;