CREATE TABLE mir15invoice (
  mir15pk BIGINT NOT NULL AUTO_INCREMENT,
  mir15code VARCHAR(30) NULL,
  mir15name VARCHAR(250) NULL,
  mir15state INT(10) UNSIGNED NOT NULL DEFAULT 1,
  sis10pk BIGINT NOT NULL,
  PRIMARY KEY(mir15pk),
  FOREIGN KEY(sis10pk)
    REFERENCES mir04employee(sis10pk)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
)
ENGINE=InnoDB;

-- Alter Worklog table
alter table mir10worklog add column mir15pk bigint;
alter table mir10worklog add constraint FOREIGN KEY(mir15pk) REFERENCES mir15invoice(mir15pk) ON DELETE NO ACTION;
