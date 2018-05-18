UPDATE `retradb`.`mir03activity` SET `mir03code`='IT-Doc' WHERE `mir03pk`='3030005';
UPDATE `retradb`.`mir03activity` SET `mir03code`='IT-Adm' WHERE `mir03pk`='3030015';
UPDATE `retradb`.`mir03activity` SET `mir03code`='IT-Meet' WHERE `mir03pk`='3030018';
INSERT INTO `retradb`.`mir03activity` (`mir03pk`, `mir03code`, `mir03name`, `mir03description`, `mir03state`) VALUES ('3030025', 'OPR-Cons', 'Consultation', 'Consultation problems and Solutions', '1');
INSERT INTO `retradb`.`mir03activity` (`mir03pk`, `mir03code`, `mir03name`, `mir03description`, `mir03state`) VALUES ('3030026', 'OPR-Spec', 'Business specification', 'Business specification', '1');
INSERT INTO `retradb`.`mir03activity` (`mir03pk`, `mir03code`, `mir03name`, `mir03description`, `mir03state`) VALUES ('3030027', 'OPR-Test', 'Testing', 'System or processes testing.', '1');
INSERT INTO `retradb`.`mir03activity` (`mir03pk`, `mir03code`, `mir03name`, `mir03description`, `mir03state`) VALUES ('3030028', 'OPR-Meet', 'Meeting', 'Meeting.', '1');
INSERT INTO `retradb`.`mir03activity` (`mir03pk`, `mir03code`, `mir03name`, `mir03description`, `mir03state`) VALUES ('3030029', 'OPR-Adm', 'Administrative activities', 'Mails, calls, paper work, work reports, etc.', '1');
