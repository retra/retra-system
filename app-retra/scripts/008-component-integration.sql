-- Create new table for components
CREATE TABLE mir12component (
  mir12pk BIGINT NOT NULL AUTO_INCREMENT,
  mir12projectPk BIGINT NULL,
  mir12code VARCHAR(30) NULL,
  mir12name VARCHAR(250) NULL,
  mir12state INT(10) UNSIGNED NOT NULL DEFAULT 1,
  PRIMARY KEY(mir12pk),
  FOREIGN KEY(mir12projectPk)
    REFERENCES mir01project(mir01pk)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
)
ENGINE=InnoDB;

-- Alter Worklog table
alter table mir10worklog add column mir12pk bigint;
alter table mir10worklog add constraint FOREIGN KEY(mir12pk) REFERENCES mir12component(mir12pk) ON DELETE NO ACTION;
