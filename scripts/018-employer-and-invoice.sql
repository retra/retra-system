-- create icompany table
CREATE TABLE mir18icompany (
  mir18pk BIGINT NOT NULL AUTO_INCREMENT,
  mir18code VARCHAR(30) NULL,
  mir18name VARCHAR(250) NULL,
  mir18state INT(10) UNSIGNED NOT NULL DEFAULT 1,
  PRIMARY KEY(mir18pk)
)
ENGINE=InnoDB;

-- alter employee table
alter table mir04employee add column mir04igenerate boolean default null;
alter table mir04employee add column mir18pk bigint default null;
alter table mir04employee add constraint FOREIGN KEY(mir18pk) REFERENCES mir18icompany(mir18pk) ON DELETE NO ACTION;

-- insert default companies
INSERT INTO mir18icompany (mir18pk, mir18code, mir18name, mir18state) VALUES (3180001, 'ART', 'ARTIN', 1);
INSERT INTO mir18icompany (mir18pk, mir18code, mir18name, mir18state) VALUES (3180002, 'AVT', 'AVInt', 1);
INSERT INTO mir18icompany (mir18pk, mir18code, mir18name, mir18state) VALUES (3180003, 'AW', 'AspectWorks', 1);
INSERT INTO mir18icompany (mir18pk, mir18code, mir18name, mir18state) VALUES (3180004, 'CPR', 'Capricorns', 1);
INSERT INTO mir18icompany (mir18pk, mir18code, mir18name, mir18state) VALUES (3180005, 'PCE', 'Principal E.', 1);
INSERT INTO mir18icompany (mir18pk, mir18code, mir18name, mir18state) VALUES (3180006, 'YS', 'YourSystem', 1);
