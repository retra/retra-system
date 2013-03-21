

-- Import projects ...

INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010001, NULL, 'AWP', 'AspectWorks Projects');

INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010002, NULL, 'OSP', 'OpenSource Projects');

INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010010, 3010002, 'MIRA', 'Managing Internal Resource Application');

INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010020, 3010001, 'IMA', 'Interactive Media Affiliate');
INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010021, 3010001, 'IMP', 'Interactive Media Public');
INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010022, 3010001, 'IMT', 'IMT');
INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010023, 3010001, 'PPX', 'Popularix');
INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010024, 3010001, 'PTP', 'PTP');
INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010025, 3010001, 'ADM', 'Administration');
INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010026, 3010001, 'BSC', 'BSC');
INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010027, 3010001, 'B2B', 'B2B');
INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010028, 3010001, 'PelAS', 'PelAS');
INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010029, 3010001, 'AWI', 'AWI');
INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010030, 3010001, 'Snadne', 'Portál Snadně.CZ');

-- Import Activities

INSERT INTO mir03activity (mir03pk, mir01pk, mir03code, mir03name) VALUES (3030001, NULL, 'PM', 'Project Management');
INSERT INTO mir03activity (mir03pk, mir01pk, mir03code, mir03name) VALUES (3030002, NULL, 'SPEC', 'Functional, business and technical specification');
INSERT INTO mir03activity (mir03pk, mir01pk, mir03code, mir03name) VALUES (3030003, NULL, 'PROG', 'Programming');
INSERT INTO mir03activity (mir03pk, mir01pk, mir03code, mir03name) VALUES (3030004, NULL, 'TEST', 'Testing');
INSERT INTO mir03activity (mir03pk, mir01pk, mir03code, mir03name) VALUES (3030005, NULL, 'DOC', 'Documenting');
INSERT INTO mir03activity (mir03pk, mir01pk, mir03code, mir03name) VALUES (3030006, NULL, 'ADM', 'System Administration');

-- Import default users ...

INSERT INTO sis12contactinfo (sis12pk, sis12firstName, sis12lastName, sis12email, sis12web, sis12phone1, sis12phone2, sis12fax) VALUES (1120001, 'Admin', 'Boss', 'admin@mira.aspectworks.com', 'http://www.aspectworks.com/', '', '', '');
INSERT INTO sis13role (sis13pk, sis13roleType, sis13id, sis13name, sis13description, sis13type ) VALUES (1130001, NULL, 'personalRole.admin', 'Admin Personal Role', '', 1);
INSERT INTO sis10user (sis10pk, sis12pk, sis13pk) VALUES (1100001, 1120001, 1130001);
INSERT INTO sis11login (sis11pk, sis10pk, sis11name, sis11password, sis11state) VALUES (1110001, 1100001, 'admin', md5('password'), '1');
INSERT INTO mir04employee (sis10pk) VALUES (1100001);

INSERT INTO sis12contactinfo (sis12pk, sis12firstName, sis12lastName, sis12email, sis12web, sis12phone1, sis12phone2, sis12fax) VALUES (1120002, 'Radek', 'Pinc', 'radek.pinc@aspectworks.com', 'http://www.aspectworks.com/', '', '', '');
INSERT INTO sis13role (sis13pk, sis13roleType, sis13id, sis13name, sis13description, sis13type ) VALUES (1130002, NULL, 'personalRole.radek', 'Radek Personal Role', '', 1);
INSERT INTO sis10user (sis10pk, sis12pk, sis13pk) VALUES (1100002, 1120002, 1130002);
INSERT INTO sis11login (sis11pk, sis10pk, sis11name, sis11password, sis11state) VALUES (1110002, 1100002, 'radek', md5('password'), '1');
INSERT INTO mir04employee (sis10pk) VALUES (1100002);

INSERT INTO sis12contactinfo (sis12pk, sis12firstName, sis12lastName, sis12email, sis12web, sis12phone1, sis12phone2, sis12fax) VALUES (1120003, 'Petr', 'Sigl', 'petr.sigl@aspectworks.com', 'http://www.aspectworks.com/', '', '', '');
INSERT INTO sis13role (sis13pk, sis13roleType, sis13id, sis13name, sis13description, sis13type ) VALUES (1130003, NULL, 'personalRole.petr', 'Petr Personal Role', '', 1);
INSERT INTO sis10user (sis10pk, sis12pk, sis13pk) VALUES (1100003, 1120003, 1130003);
INSERT INTO sis11login (sis11pk, sis10pk, sis11name, sis11password, sis11state) VALUES (1110003, 1100003, 'petr', md5('password'), '1');
INSERT INTO mir04employee (sis10pk) VALUES (1100003);

-- Import Types

insert into mir07type (mir07pk, mir07code, mir07name, mir07description) values (3010001, 'IPRJ', 'Internal project', 'Use this if not defined any type of schedule.');
insert into mir07type (mir07pk, mir07code, mir07name, mir07description) values (3010002, 'HOL', 'Holidays', 'Use for holidays or vacation.');
insert into mir07type (mir07pk, mir07code, mir07name, mir07description) values (3010003, 'BSHP', 'Bodyshop', 'Use for bodyshop.');
insert into mir07type (mir07pk, mir07code, mir07name, mir07description) values (3010004, 'BTRP', 'Bussiness trip', 'Use for  bussiness trip.');

  