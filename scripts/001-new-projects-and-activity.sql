
UPDATE mir01project SET mir01code = 'AW-PRJS' WHERE mir01pk = 3010001;
UPDATE mir01project SET mir01code = 'OS-PRJS' WHERE mir01pk = 3010002;

INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010031, 3010001, 'AWP', 'AspectWorks Processes');

INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010032, 3010001, 'HR', 'Human Resources');
INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010033, 3010001, 'SALES', 'Sales');
INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010034, 3010001, 'GP', 'General Processes');
INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010035, 3010001, 'OWN', 'Owners');
INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010036, 3010001, 'MGMT', 'Management');
INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010037, 3010001, 'MRKT', 'Marketing');
INSERT INTO mir01project (mir01pk, mir01parentPk, mir01code, mir01name) VALUES (3010038, 3010001, 'EDU', 'Education');



INSERT INTO mir03activity (mir03pk, mir01pk, mir03code, mir03name) VALUES (3030007, NULL, 'HR', 'Human Resources');
INSERT INTO mir03activity (mir03pk, mir01pk, mir03code, mir03name) VALUES (3030008, NULL, 'FIN', 'Finance');
INSERT INTO mir03activity (mir03pk, mir01pk, mir03code, mir03name) VALUES (3030009, NULL, 'OTHER', 'Other Internal Processes');

INSERT INTO mir03activity (mir03pk, mir01pk, mir03code, mir03name) VALUES (3030010, NULL, 'TRAVEL', 'Traveling');
INSERT INTO mir03activity (mir03pk, mir01pk, mir03code, mir03name) VALUES (3030011, NULL, 'EMAIL', 'Email communication');
INSERT INTO mir03activity (mir03pk, mir01pk, mir03code, mir03name) VALUES (3030012, NULL, 'PHONE', 'Phone communication');
INSERT INTO mir03activity (mir03pk, mir01pk, mir03code, mir03name) VALUES (3030013, NULL, 'VERB', 'Verbal communication');


