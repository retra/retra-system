CREATE TABLE mir16invoiceseq (
  mir16pk BIGINT NOT NULL AUTO_INCREMENT,
  mir16code VARCHAR(30) NULL,
  mir16name VARCHAR(250) NULL,
  mir16pattern VARCHAR(30) NULL,
  mir16sequence BIGINT NOT NULL DEFAULT 0,
  mir16state INT(10) UNSIGNED NOT NULL DEFAULT 1,
  PRIMARY KEY(mir16pk)
)
ENGINE=InnoDB;

-- this is example sequence, could be commented if you do not want it
INSERT INTO mir16invoiceseq VALUES (null, '2012V', 'Vývoj 2012', '\'2012V\'0000', 0, 1);