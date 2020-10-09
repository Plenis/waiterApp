create table users(
	id serial not null primary key,
	firstname text,
	lastname text,
	username text,
	password text
);

insert into users (id, firstname, lastname, username, password) VALUES (1, 'Sino', 'Plenis', 'SPlenis', '123');