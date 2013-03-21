SET FOREIGN_KEY_CHECKS=0;

delete from mir01project;

insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010001, null, 'AW-P-PRJS', 'AspectWorks Paid Projects', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010002, null, 'OS-PRJS', 'OpenSource Projects', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010003, null, 'AW-I-PRJS', 'AspectWorks Internal Projects', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010010, 3010002, 'MIRA-OS', 'Managing Internal Resource Application', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010011, 3010002, 'KAPR', 'Information System VZSK', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010020, 3010001, 'IMA-01', 'Interactive Media Affiliate', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010021, 3010001, 'IMP-01', 'Interactive Media Public', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010022, 3010001, 'IMT-01', 'IMT', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010023, 3010001, 'PPX-01', 'Popularix', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010024, 3010001, 'PPT-01', 'Potenza', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010025, 3010003, '0adm', 'Administrative, office work', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010026, 3010001, 'BSC-01', 'BSC systems and ideas', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010027, 3010001, 'B2B-01', 'B2B Centrum', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010028, 3010001, 'PelAS-01', 'PelAS', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010029, 3010001, 'AWI', 'AWI', 0);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010030, 3010001, 'Snadne-01', 'Portel Snadne.CZ', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010031, 3010001, 'AWP', 'AspectWorks Processes', 0);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010032, 3010003, '0hr', 'Human Resources', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010033, 3010003, '0sal', 'Sales', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010034, 3010001, 'GP', 'General Processes', 0);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010035, 3010001, 'OWN', 'Owners', 0);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010036, 3010003, '0mgt', 'Management', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010037, 3010003, '0mktg-01', 'Marketing analysis, AW vision.', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010038, 3010003, '0tra', 'Training, education', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010039, 3010001, 'DMD', 'Digital Marketing Cockpit Development', 0);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010040, 3010003, '0awis', 'Internal collaboration software', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010041, 3010003, '0iso', 'ISO 9001 norm', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010042, 3010003, '0fin', 'Financial background', 1);
insert into mir01project (mir01pk, mir01parentPk, mir01code, mir01name, mir01state) values (3010043, 3010003, '0prg', 'Programming, system servicing', 1);

delete from mir03activity;

insert into mir03activity (mir03pk, mir01pk, mir03code, mir03name, mir03description, mir03state) values (3030001, null, 'PM', 'Project Management', 'Project or team leading, organising, planning, communication, etc. Leaders and PM''s only. ', 1);
insert into mir03activity (mir03pk, mir01pk, mir03code, mir03name, mir03description, mir03state) values (3030002, null, 'IT-Spec', 'Business specification, analysis', 'Analysis, function and business specification. ', 1);
insert into mir03activity (mir03pk, mir01pk, mir03code, mir03name, mir03description, mir03state) values (3030003, null, 'IT-Prg', 'Programming', 'All type of languages.', 1);
insert into mir03activity (mir03pk, mir01pk, mir03code, mir03name, mir03description, mir03state) values (3030004, null, 'IT-Test', 'Testing', 'System or processes testing.', 1);
insert into mir03activity (mir03pk, mir01pk, mir03code, mir03name, mir03description, mir03state) values (3030005, null, 'Doc', 'Documentation', 'Creating and writing manuals, presentations, guidelines, notes, wiki, etc. ', 1);
insert into mir03activity (mir03pk, mir01pk, mir03code, mir03name, mir03description, mir03state) values (3030006, null, 'IT-Adm', 'System Administration', 'Installation and services that keep systems running, except program writing.', 1);
insert into mir03activity (mir03pk, mir01pk, mir03code, mir03name, mir03description, mir03state) values (3030008, null, 'FIN', 'Finance', null, 0);
insert into mir03activity (mir03pk, mir01pk, mir03code, mir03name, mir03description, mir03state) values (3030009, null, 'OTHER', 'Other Internal Processes', null, 0);
insert into mir03activity (mir03pk, mir01pk, mir03code, mir03name, mir03description, mir03state) values (3030010, null, 'Travel', 'Traveling', 'Traveling to meetings, trainings or another work place. Doesn''t include travel from and to home.', 1);
insert into mir03activity (mir03pk, mir01pk, mir03code, mir03name, mir03description, mir03state) values (3030011, null, 'EMAIL', 'Email communication', null, 0);
insert into mir03activity (mir03pk, mir01pk, mir03code, mir03name, mir03description, mir03state) values (3030012, null, 'PHONE', 'Phone communication', null, 0);
insert into mir03activity (mir03pk, mir01pk, mir03code, mir03name, mir03description, mir03state) values (3030013, null, 'VERB', 'Verbal communication', null, 0);
insert into mir03activity (mir03pk, mir01pk, mir03code, mir03name, mir03description, mir03state) values (3030014, null, 'Act', 'Accounting', 'Accounting, financial reports.', 1);
insert into mir03activity (mir03pk, mir01pk, mir03code, mir03name, mir03description, mir03state) values (3030015, null, 'Adm', 'Administrative activities', 'Mails, calls, paper work, work reports, etc.', 1);
insert into mir03activity (mir03pk, mir01pk, mir03code, mir03name, mir03description, mir03state) values (3030016, null, 'IT-Dsg', 'Software Design', 'Design and application analysis.', 1);
insert into mir03activity (mir03pk, mir01pk, mir03code, mir03name, mir03description, mir03state) values (3030017, null, 'Meet-C', 'Meeting with client', 'Business meeting with non-AW employee.', 1);
insert into mir03activity (mir03pk, mir01pk, mir03code, mir03name, mir03description, mir03state) values (3030018, null, 'Meet-I', 'Meeting internal', 'Internal meeting within AW family.', 1);
insert into mir03activity (mir03pk, mir01pk, mir03code, mir03name, mir03description, mir03state) values (3030019, null, 'Pre', 'Preparation', 'Preparation for project or special work. Garthering and collecting information, manuals, guidelines etc. ', 1);

SET FOREIGN_KEY_CHECKS=1;