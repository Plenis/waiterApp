create table users(
	id serial not null primary key,
	firstname text,
	lastname text,
	username text,
	password text
);

insert into users (id, firstname, lastname, username, password) VALUES (1, 'Sino', 'Plenis', 'SPlenis', '123');
insert into users (id, firstname, lastname, username, password) VALUES (2, 'John', 'Doe', 'JD', '111');
insert into users (id, firstname, lastname, username, password) VALUES (3, 'Ben', 'Ten', 'B10', '110');


ALTER SEQUENCE users_id_seq RESTART WITH 4;
