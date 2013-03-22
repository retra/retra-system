alter table mir01project add column mir01workenabled boolean;

alter table mir01project add column mir01estimation decimal(24,2);

update mir01project set mir01workenabled=true;

CREATE TABLE mir17category (
  mir17pk BIGINT NOT NULL AUTO_INCREMENT,
  mir01pk BIGINT NULL,
  mir17code VARCHAR(30) NULL,
  mir17name VARCHAR(250) NULL,
  mir17description VARCHAR(250) NULL,
  mir17state INT(10) UNSIGNED NOT NULL DEFAULT 1,
  PRIMARY KEY(mir17pk),
  FOREIGN KEY(mir01pk)
    REFERENCES mir01project(mir01pk)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
)
ENGINE=InnoDB;

-- Alter Project table
alter table mir01project add column mir17pk bigint;
alter table mir01project add constraint FOREIGN KEY(mir17pk) REFERENCES mir17category(mir17pk) ON DELETE NO ACTION;

INSERT INTO mir17category (mir17pk, mir01pk, mir17code, mir17name) VALUES (3170001, NULL, 'BRD', 'Board');
INSERT INTO mir17category (mir17pk, mir01pk, mir17code, mir17name) VALUES (3170002, NULL, 'ZV', 'Events committee');
INSERT INTO mir17category (mir17pk, mir01pk, mir17code, mir17name) VALUES (3170003, NULL, 'INT', 'Internal');
INSERT INTO mir17category (mir17pk, mir01pk, mir17code, mir17name) VALUES (3170004, NULL, 'OWA', 'Others');


 
