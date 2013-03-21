

-- Add permanentPassword to login

ALTER TABLE sis11login 
	ADD sis11permanentPassword VARCHAR(250) DEFAULT NULL 
	AFTER sis11password;