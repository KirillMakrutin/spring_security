-- Calendar Users
insert into calendar_users(id,email,password,first_name,last_name) values (0,'user1@example.com','38aab7ba97bd6bb2e51add1e5617eabfc8d13ec85c004e909eec4b70172437ae85e0c56e43fe51b0','User','1');
insert into calendar_users(id,email,password,first_name,last_name) values (1,'admin1@example.com','98afcd6f54569da7fea7fe4b1bf79d59dd27e559d38ee75cabd796f43058ebe15f201dfd453942e0','Admin','1');
insert into calendar_users(id,email,password,first_name,last_name) values (2,'user2@example.com','429d7af2097fb1a0a3a4050bff17d8189cb2244aef52476cad2fef3bcc7338078dbf73644494554a','User','2');

-- ROLES --
insert into role(id, name) values (0, 'ROLE_USER');
insert into role(id, name) values (1, 'ROLE_ADMIN');


-- User have one role
insert into user_role(user_id,role_id) values (0, 0);

-- Admin has two roles
insert into user_role(user_id,role_id) values (1, 0);
insert into user_role(user_id,role_id) values (1, 1);

-- Events --
insert into events (id,when,summary,description,owner,attendee) values (100,'2017-07-03 20:30:00','Birthday Party','This is going to be a great birthday',0,1);
insert into events (id,when,summary,description,owner,attendee) values (101,'2017-12-23 13:00:00','Conference Call','Call with the client',2,0);
insert into events (id,when,summary,description,owner,attendee) values (102,'2017-09-14 11:30:00','Vacation','Paragliding in Greece',1,2);

