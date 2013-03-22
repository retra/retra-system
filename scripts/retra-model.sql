CREATE TABLE sis18assign (
  sis18roleJoinType BIGINT(20) NOT NULL,
  sis18roleJoinFeature BIGINT(20) NOT NULL,
  PRIMARY KEY(sis18roleJoinType, sis18roleJoinFeature)
)
ENGINE=InnoDB;

CREATE TABLE sis20object (
  sis20pk BIGINT(20) NOT NULL,
  sis20class BIGINT(20) NOT NULL,
  sis20id VARCHAR(120) NOT NULL,
  PRIMARY KEY(sis20pk)
)
ENGINE=InnoDB;

CREATE TABLE sis21class (
  sis21pk BIGINT(20) NOT NULL,
  sis21id VARCHAR(120) NOT NULL,
  PRIMARY KEY(sis21pk)
)
ENGINE=InnoDB;

CREATE TABLE sis17feature (
  sis17pk BIGINT(20) NOT NULL,
  sis17name VARCHAR(60) NOT NULL,
  PRIMARY KEY(sis17pk)
)
ENGINE=InnoDB;

CREATE TABLE sis14type (
  sis14pk BIGINT(20) NOT NULL,
  sis14id VARCHAR(60) NOT NULL,
  PRIMARY KEY(sis14pk)
)
ENGINE=InnoDB;

CREATE TABLE sis15join (
  sis15pk BIGINT(20) NOT NULL,
  sis15first BIGINT(20) NOT NULL,
  sis15second BIGINT(20) NOT NULL,
  PRIMARY KEY(sis15pk)
)
ENGINE=InnoDB;

CREATE TABLE sis16type (
  sis16pk BIGINT(20) NOT NULL,
  PRIMARY KEY(sis16pk)
)
ENGINE=InnoDB;

CREATE TABLE sys02range (
  sys02pk BIGINT(20) NOT NULL,
  sys00pk BIGINT(20) NOT NULL,
  sys02begin BIGINT(20) NOT NULL,
  sys02end BIGINT(20) NOT NULL,
  PRIMARY KEY(sys02pk)
)
ENGINE=InnoDB;

CREATE TABLE sys03journal (
  sys03pk BIGINT(20) NOT NULL,
  sys03timestamp DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00',
  sys03chanel VARCHAR(30) NOT NULL,
  sys03source VARCHAR(60) NOT NULL,
  sys03author VARCHAR(30) NOT NULL,
  sys03entity VARCHAR(30) NOT NULL,
  sys03entitypk BIGINT(20) NOT NULL,
  sys03operation VARCHAR(30) NOT NULL,
  sys03sql TEXT NOT NULL,
  PRIMARY KEY(sys03pk)
);

CREATE TABLE sys04ticket (
  sys04id VARCHAR(60) NOT NULL,
  sys04name VARCHAR(60) NOT NULL,
  sys04created DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00'
)
ENGINE=InnoDB;

CREATE TABLE sys01sequence (
  sys01number BIGINT(20) NOT NULL,
  PRIMARY KEY(sys01number)
)
ENGINE=InnoDB;

CREATE TABLE sis22attribute (
  sis22pk BIGINT(20) NOT NULL,
  sis22object BIGINT(20) NOT NULL,
  sis22name VARCHAR(60) NOT NULL,
  sis22title VARCHAR(250) NOT NULL,
  sis22type CHAR(1) NOT NULL DEFAULT 'S',
  sis22businessType VARCHAR(250) NOT NULL,
  PRIMARY KEY(sis22pk)
)
ENGINE=InnoDB;

CREATE TABLE sis23value (
  sis23pk BIGINT(20) NOT NULL,
  sis23attribute BIGINT(20) NOT NULL,
  sis23role BIGINT(20) NOT NULL,
  sis23type DECIMAL(2,0) NOT NULL,
  sis23value VARCHAR(250) NOT NULL,
  PRIMARY KEY(sis23pk)
)
ENGINE=InnoDB;

CREATE TABLE sys00info (
  sys00pk BIGINT(20) NOT NULL,
  sys00id VARCHAR(60) NOT NULL,
  sys00title VARCHAR(250) NOT NULL,
  sys00major INTEGER(11) NOT NULL,
  sys00minior INTEGER(11) NOT NULL,
  sys00patchlevel INTEGER(11) NOT NULL,
  sys00current INTEGER(11) NOT NULL,
  sys00state INTEGER(11) NOT NULL
)
ENGINE=InnoDB;

CREATE TABLE mir01project (
  mir01pk BIGINT NOT NULL AUTO_INCREMENT,
  mir01parentPk BIGINT NULL,
  mir01code VARCHAR(30) NULL,
  mir01name VARCHAR(250) NULL,
  PRIMARY KEY(mir01pk),
  FOREIGN KEY(mir01parentPk)
    REFERENCES mir01project(mir01pk)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
)
ENGINE=InnoDB;

CREATE TABLE sis09counter (
  sis09pk BIGINT(20) NOT NULL,
  sis09id VARCHAR(60) NOT NULL,
  sis09value DECIMAL(21,0) NOT NULL,
  PRIMARY KEY(sis09pk)
)
ENGINE=InnoDB;

CREATE TABLE mir05role (
  mir05pk BIGINT NOT NULL AUTO_INCREMENT,
  mir05id VARCHAR(60) NULL,
  mir05name VARCHAR(250) NULL,
  mir05description TEXT NULL,
  PRIMARY KEY(mir05pk)
)
ENGINE=InnoDB;

CREATE TABLE mir07type (
  mir07pk BIGINT NOT NULL AUTO_INCREMENT,
  mir07code VARCHAR(255) NULL,
  mir07name VARCHAR(255) NULL,
  mir07description VARCHAR(255) NULL,
  PRIMARY KEY(mir07pk)
)
ENGINE=InnoDB;

CREATE TABLE sis12contactinfo (
  sis12pk BIGINT NOT NULL AUTO_INCREMENT,
  sis12firstName VARCHAR(60) NOT NULL,
  sis12lastName VARCHAR(60) NOT NULL,
  sis12email VARCHAR(250) NOT NULL,
  sis12web VARCHAR(250) NOT NULL,
  sis12phone1 VARCHAR(30) NOT NULL,
  sis12phone2 VARCHAR(30) NOT NULL,
  sis12fax VARCHAR(30) NOT NULL,
  PRIMARY KEY(sis12pk)
)
ENGINE=InnoDB;

CREATE TABLE sis13role (
  sis13pk BIGINT NOT NULL AUTO_INCREMENT,
  sis13roleType BIGINT(20) NULL,
  sis13id VARCHAR(60) NOT NULL,
  sis13name VARCHAR(250) NOT NULL,
  sis13description TEXT NULL,
  sis13type INTEGER UNSIGNED NULL,
  PRIMARY KEY(sis13pk)
)
ENGINE=InnoDB;

CREATE TABLE mir03activity (
  mir03pk BIGINT NOT NULL AUTO_INCREMENT,
  mir01pk BIGINT NULL,
  mir03code VARCHAR(30) NULL,
  mir03name VARCHAR(250) NULL,
  mir03description VARCHAR(250) NULL,
  PRIMARY KEY(mir03pk),
  FOREIGN KEY(mir01pk)
    REFERENCES mir01project(mir01pk)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
)
ENGINE=InnoDB;

CREATE TABLE mir02task (
  mir02task BIGINT NOT NULL AUTO_INCREMENT,
  mir02parentPk BIGINT NOT NULL,
  mir01pk BIGINT NOT NULL,
  mir02id VARCHAR(30) NULL,
  mir02title VARCHAR(250) NULL,
  mir02description TEXT NULL,
  PRIMARY KEY(mir02task),
  FOREIGN KEY(mir01pk)
    REFERENCES mir01project(mir01pk)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(mir02parentPk)
    REFERENCES mir02task(mir02task)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
)
ENGINE=InnoDB;

CREATE TABLE sis10user (
  sis10pk BIGINT NOT NULL AUTO_INCREMENT,
  sis12pk BIGINT NOT NULL,
  sis13pk BIGINT NOT NULL,
  PRIMARY KEY(sis10pk),
  FOREIGN KEY(sis13pk)
    REFERENCES sis13role(sis13pk)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(sis12pk)
    REFERENCES sis12contactinfo(sis12pk)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
)
ENGINE=InnoDB;

CREATE TABLE mir04employee (
  sis10pk BIGINT NOT NULL,
  mir04aaa INTEGER UNSIGNED NULL,
  PRIMARY KEY(sis10pk),
  FOREIGN KEY(sis10pk)
    REFERENCES sis10user(sis10pk)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
)
ENGINE=InnoDB;

CREATE TABLE sis11login (
  sis11pk BIGINT NOT NULL AUTO_INCREMENT,
  sis10pk BIGINT NOT NULL,
  sis11name VARCHAR(60) NOT NULL,
  sis11password VARCHAR(60) NOT NULL,
  sis11state INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(sis11pk),
  INDEX sis11login_unique_name(sis11name),
  FOREIGN KEY(sis10pk)
    REFERENCES sis10user(sis10pk)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
)
ENGINE=InnoDB;

CREATE TABLE mir11schedule (
  mir11pk BIGINT NOT NULL AUTO_INCREMENT,
  mir07pk BIGINT NOT NULL,
  sis10pk BIGINT NOT NULL,
  mir11from DATETIME NULL,
  mir11to DATETIME NULL,
  mir11hours DECIMAL(6,2) NULL,
  mir11state INTEGER UNSIGNED NULL DEFAULT 0,
  mir11comment VARCHAR(250) NULL DEFAULT 0,
  mir11level INTEGER UNSIGNED NULL,
  mir11createdOn DATETIME NULL,
  mir11changedOn DATETIME NULL,
  PRIMARY KEY(mir11pk),
  FOREIGN KEY(sis10pk)
    REFERENCES mir04employee(sis10pk)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(mir07pk)
    REFERENCES mir07type(mir07pk)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
)
ENGINE=InnoDB;

CREATE TABLE mir06assign (
  mir06pk BIGINT NOT NULL AUTO_INCREMENT,
  sis10pk BIGINT NOT NULL,
  mir05pk BIGINT NOT NULL,
  mir01pk BIGINT NOT NULL,
  PRIMARY KEY(mir06pk),
  FOREIGN KEY(mir01pk)
    REFERENCES mir01project(mir01pk)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(mir05pk)
    REFERENCES mir05role(mir05pk)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(sis10pk)
    REFERENCES mir04employee(sis10pk)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
)
ENGINE=InnoDB;

CREATE TABLE mir10worklog (
  mir10pk BIGINT NOT NULL AUTO_INCREMENT,
  sis10pk BIGINT NOT NULL,
  mir03pk BIGINT NOT NULL,
  mir01pk BIGINT NOT NULL,
  mir02task BIGINT NULL,
  mir10from DATETIME NULL,
  mir10to DATETIME NULL,
  mir10description VARCHAR(250) NULL,
  PRIMARY KEY(mir10pk),
  FOREIGN KEY(mir01pk)
    REFERENCES mir01project(mir01pk)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(mir02task)
    REFERENCES mir02task(mir02task)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(mir03pk)
    REFERENCES mir03activity(mir03pk)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(sis10pk)
    REFERENCES mir04employee(sis10pk)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
)
ENGINE=InnoDB;

