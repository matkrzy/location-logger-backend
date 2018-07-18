/*
    INSERT USER
*/
INSERT INTO user (id,username,password_hash,active,role) VALUES (1,'admin@admin.com','$2a$10$NU8oqQaEXPG/KrH2fOurTuLjdjZ5TISAhOx7.XLkaAMgClwTIAujG',1,'ADMIN');
INSERT INTO user (id,username,password_hash,active,role) VALUES (2,'hamilton@gmail.com','$2a$10$NU8oqQaEXPG/KrH2fOurTuLjdjZ5TISAhOx7.XLkaAMgClwTIAujG',1,'USER');
INSERT INTO user (id,username,password_hash,active,role) VALUES (3,'kludge@att.net','$2a$10$NU8oqQaEXPG/KrH2fOurTuLjdjZ5TISAhOx7.XLkaAMgClwTIAujG',1,'USER');
INSERT INTO user (id,username,password_hash,active,role) VALUES (4,'linuxhack@sbcglobal.net','$2a$10$NU8oqQaEXPG/KrH2fOurTuLjdjZ5TISAhOx7.XLkaAMgClwTIAujG',1,'USER');
INSERT INTO user (id,username,password_hash,active,role) VALUES (5,'qmacro@mac.com','$2a$10$NU8oqQaEXPG/KrH2fOurTuLjdjZ5TISAhOx7.XLkaAMgClwTIAujG',0,'USER');

/*
    INSERT DEVICE
*/

INSERT INTO device (id, `name`, removed, user_id, uuid) VALUES (1,'Iphone 5s',b'0',1,'787b559d-bd34-4a8f-94e3-06afdd15dfb0');
INSERT INTO device (id, `name`, removed, user_id, uuid) VALUES (2,'Xiaomi 4A',b'0',1,'a1a241c7-116a-4347-bd69-d53c5f6a4427');
