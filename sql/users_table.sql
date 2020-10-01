create table users(
	id serial not null primary key,
	firstname text,
	lastname text,
	username text,
	password text
);

insert into users (firstname, lastname, username, password) VALUES ("Sino", "Plenis", "SPlenis", "123");