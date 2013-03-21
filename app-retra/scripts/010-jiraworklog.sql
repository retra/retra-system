
-- Table for storing worklog and JIRA issue relation
CREATE TABLE mir14jiraworklog (
  `mir14pk` bigint(20) NOT NULL auto_increment,
  `sis10pk` bigint(20) NOT NULL,
  `mir10pk` bigint(20) default NULL,
  `mir14jiraIssue` varchar(50) NOT NULL,
  `mir14created` datetime NOT NULL,
  `mir14timeSpentInSeconds` bigint(20) NOT NULL,
  `mir14state` int NOT NULL,
  PRIMARY KEY  (`mir14pk`),
  KEY (`sis10pk`),
  KEY (`mir10pk`),
  CONSTRAINT FOREIGN KEY (`mir10pk`) REFERENCES `mir10worklog` (`mir10pk`),
  CONSTRAINT FOREIGN KEY (`sis10pk`) REFERENCES `mir04employee` (`sis10pk`)
)ENGINE=InnoDB;

ALTER TABLE sis12contactinfo ADD COLUMN sis12jirauser varchar(50) null;

