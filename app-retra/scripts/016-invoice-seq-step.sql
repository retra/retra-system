alter table mir16invoiceseq add column mir16step INT(10) UNSIGNED NOT NULL DEFAULT 1;

--update mir16invoiceseq set mir16step=3 where mir16code='2012V'