-- New table for the assignment of Employees to Projects
CREATE TABLE `mir13link_employee_project` (
  `sis10pk` bigint(20) NOT NULL,
  `mir01pk` bigint(20) NOT NULL,
  PRIMARY KEY  (`sis10pk`,`mir01pk`),
  KEY (`sis10pk`),
  KEY (`mir01pk`),
  CONSTRAINT FOREIGN KEY (`mir01pk`) REFERENCES `mir01project` (`mir01pk`),
  CONSTRAINT FOREIGN KEY (`sis10pk`) REFERENCES `mir04employee` (`sis10pk`)
) ENGINE=InnoDB;

