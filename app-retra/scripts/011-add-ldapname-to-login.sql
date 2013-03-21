ALTER TABLE `mir14jiraworklog` 
  ADD COLUMN `mir14remoteId` bigint(20) NULL;

ALTER TABLE `sis11login` 
	ADD COLUMN `sis11ldaplogin` VARCHAR(60) DEFAULT NULL 
	AFTER `sis11name`;

ALTER TABLE `sis11login` 
	ADD UNIQUE `unique_sis11ldaplogin` 
	USING BTREE(`sis11ldaplogin`);	