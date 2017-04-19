
-- Retra 3 - Script 01

CREATE TABLE mir20workday (
	mir20pk BIGINT NOT NULL,
	mir20date DATE NOT NULL,
	mir20state VARCHAR(30) NOT NULL,
	INDEX mir20idx01 (mir20state, mir20date),
	UNIQUE KEY (mir20date),
	PRIMARY KEY (mir20pk)
);

CREATE TABLE mir21place (
	mir21pk BIGINT NOT NULL,
	mir21code VARCHAR(30) NOT NULL,
	mir21type VARCHAR(30) NOT NULL,
	mir21parent BIGINT NULL,
	mir21description VARCHAR(255) NOT NULL DEFAULT "",
	mir21extrnalLink VARCHAR(255) DEFAULT NULL,
	sis10pk BIGINT NULL, -- Primarni uzivatel
	sis13pk BIGINT NULL, -- Primarni skupina (team)
	UNIQUE KEY (mir21code),
	PRIMARY KEY (mir21pk)
);

CREATE TABLE mir22reservation (
	mir22pk BIGINT,
	mir20pk BIGINT NOT NULL,
	mir21pk BIGINT NOT NULL,
	sis10pk BIGINT DEFAULT NULL,
	INDEX mir22idx01 (mir20pk, mir21pk),
	INDEX mir22idx02 (mir20pk, sis10pk),
	PRIMARY KEY (mir22pk)
);

CREATE TABLE mir23plan (
	mir23pk BIGINT NOT NULL,
	sis10pk BIGINT NOT NULL,
	mir23from DATE NULL,
	mir23to DATE NULL,
	mir23type VARCHAR(30) NOT NULL,
	mir23description VARCHAR(250) NOT NULL DEFAULT "",
	INDEX mir23idx01(sis10pk, mir23from, mir23to),
	PRIMARY KEY (mir23pk)
);

CREATE TABLE mir24settings (
	mir24pk BIGINT NOT NULL,
	sis10pk BIGINT NOT NULL,
	mir24notifyEmail VARCHAR(60) DEFAULT NULL,
	mir24genCalendar VARCHAR(30) NOT NULL,
	UNIQUE KEY (sis10pk),
	PRIMARY KEY (mir24pk)
);

-- Retra 3 - Script 02

CREATE TABLE sis04secureToken (
	sis04pk BIGINT NOT NULL,
	sis04bid VARCHAR(250) NOT NULL,
	sis04state VARCHAR(30) NOT NULL,
	sis04name VARCHAR(250) NOT NULL,
	sis04email VARCHAR(250) NOT NULL,
	sis04description VARCHAR(250) NOT NULL,
	sis04firstAccess DATETIME NOT NULL,
	sis04lastAccess DATETIME NOT NULL,
	PRIMARY KEY (sis04pk)
);

-- Retra 3 - Script 03

ALTER TABLE mir24settings ADD COLUMN mir24ioLastDate DATE DEFAULT NULL;
ALTER TABLE mir24settings ADD COLUMN mir24ioActivity VARCHAR(30) DEFAULT NULL;
ALTER TABLE mir24settings ADD COLUMN mir24ioCalendar VARCHAR(250) DEFAULT NULL;

-- Retra 3 - Script 04

ALTER TABLE mir20workday ADD COLUMN mir20avenirGroup VARCHAR(30) DEFAULT NULL;

-- Retra 3 - Script 05

ALTER TABLE sis13role ADD COLUMN sis13mode CHAR(1) NOT NULL DEFAULT "P" AFTER sis13roleType; 

ALTER TABLE sis14type ADD COLUMN sis14name VARCHAR(60) NOT NULL;
ALTER TABLE sis14type ADD COLUMN sis14description VARCHAR(250) NOT NULL DEFAULT "";
ALTER TABLE sis14type ADD COLUMN sis14mode CHAR(1) NOT NULL AFTER sis14pk;

INSERT INTO sis14type (sis14pk, sis14mode, sis14id, sis14name, sis14description) VALUES (140000, "P", "CIS:Personal", "Osobní role uživatelů", "Individuální role pro každého uživatele systému");
INSERT INTO sis14type (sis14pk, sis14mode, sis14id, sis14name, sis14description) VALUES (140001, "G", "OrgStrukt", "Organizační struktura", "Skupiny definující zařazení uživatelů do organizační struktury");
INSERT INTO sis14type (sis14pk, sis14mode, sis14id, sis14name, sis14description) VALUES (140002, "G", "AvenirGroup", "Avenir Skupiny", "Skupiny používané pro rotaci na Aveniru");

INSERT INTO sis13role (sis13pk, sis13roleType, sis13mode, sis13id, sis13name, sis13description) VALUES (130100, 140001, "G", "OrgStrukt:IT", "IT", "Organizační struktura: IT");
INSERT INTO sis13role (sis13pk, sis13roleType, sis13mode, sis13id, sis13name, sis13description) VALUES (130101, 140001, "G", "OrgStrukt:AP", "Aplikační podpora", "Organizační struktura: Aplikační podpora");
INSERT INTO sis13role (sis13pk, sis13roleType, sis13mode, sis13id, sis13name, sis13description) VALUES (130102, 140001, "G", "OrgStrukt:HD", "Helpdesk", "Organizační struktura: Helpdesk");
INSERT INTO sis13role (sis13pk, sis13roleType, sis13mode, sis13id, sis13name, sis13description) VALUES (130103, 140001, "G", "OrgStrukt:BI", "Business intelligence", "Organizační struktura: Business intelligence");
INSERT INTO sis13role (sis13pk, sis13roleType, sis13mode, sis13id, sis13name, sis13description) VALUES (130104, 140001, "G", "OrgStrukt:INFRA", "Infrastruktura", "Organizační struktura: Infrastruktura");
INSERT INTO sis13role (sis13pk, sis13roleType, sis13mode, sis13id, sis13name, sis13description) VALUES (130105, 140001, "G", "OrgStrukt:ITA", "IT Architektura", "Organizační struktura: IT Architektura");
INSERT INTO sis13role (sis13pk, sis13roleType, sis13mode, sis13id, sis13name, sis13description) VALUES (130106, 140001, "G", "OrgStrukt:VAPP", "Vývoj Aplikace", "Organizační struktura: Vývoj Aplikace");
INSERT INTO sis13role (sis13pk, sis13roleType, sis13mode, sis13id, sis13name, sis13description) VALUES (130107, 140001, "G", "OrgStrukt:VDB", "Vývoj Databáze", "Organizační struktura: Vývoj Databáze");
INSERT INTO sis13role (sis13pk, sis13roleType, sis13mode, sis13id, sis13name, sis13description) VALUES (130108, 140001, "G", "OrgStrukt:VTEST", "Vývoj Testování", "Organizační struktura: Vývoj Testování");
INSERT INTO sis13role (sis13pk, sis13roleType, sis13mode, sis13id, sis13name, sis13description) VALUES (130109, 140001, "G", "OrgStrukt:ITD", "IT dodávky", "Organizační struktura: IT dodávky");
INSERT INTO sis13role (sis13pk, sis13roleType, sis13mode, sis13id, sis13name, sis13description) VALUES (130110, 140001, "G", "OrgStrukt:VANAL", "Vývoj Analýza", "Organizační struktura: Vývoj Analýza");


INSERT INTO sis13role (sis13pk, sis13roleType, sis13mode, sis13id, sis13name, sis13description) VALUES (130201, 140002, "G", "AvenirGroup:S1", "Skupina 1", "Avenir Skupina 1");
INSERT INTO sis13role (sis13pk, sis13roleType, sis13mode, sis13id, sis13name, sis13description) VALUES (130202, 140002, "G", "AvenirGroup:S2", "Skupina 2", "Avenir Skupina 2");
INSERT INTO sis13role (sis13pk, sis13roleType, sis13mode, sis13id, sis13name, sis13description) VALUES (130203, 140002, "G", "AvenirGroup:S3", "Skupina 3", "Avenir Skupina 3");
INSERT INTO sis13role (sis13pk, sis13roleType, sis13mode, sis13id, sis13name, sis13description) VALUES (130204, 140002, "G", "AvenirGroup:S4", "Skupina 4", "Avenir Skupina 4");
INSERT INTO sis13role (sis13pk, sis13roleType, sis13mode, sis13id, sis13name, sis13description) VALUES (130205, 140002, "G", "AvenirGroup:S5", "Skupina 5", "Avenir Skupina 5");
INSERT INTO sis13role (sis13pk, sis13roleType, sis13mode, sis13id, sis13name, sis13description) VALUES (130206, 140002, "G", "AvenirGroup:S6", "Skupina 6", "Avenir Skupina 6");

-- Retra 3 - Script 06

ALTER TABLE mir04employee ADD COLUMN mir04worklog INT NOT NULL DEFAULT 1 AFTER mir04aaa; 
ALTER TABLE mir04employee ADD COLUMN mir04state INT NOT NULL DEFAULT 1 AFTER mir04aaa;

UPDATE mir04employee SET mir04worklog = 0 WHERE sis10pk = 1100001;

-- Retra 3 - Script 07

ALTER TABLE mir04employee DROP COLUMN mir04state;

ALTER TABLE sis10user ADD COLUMN sis10state CHAR(1) NOT NULL DEFAULT 'A';
ALTER TABLE sis10user ADD COLUMN sis10uid VARCHAR(60) DEFAULT NULL;

DELETE FROM sis15join WHERE sis15second IN (
  SELECT sis13pk FROM sis13role WHERE sis13roleType = 140001
);

DELETE FROM sis13role WHERE sis13roleType = 140001;

