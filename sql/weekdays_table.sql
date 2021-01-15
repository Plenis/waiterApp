create table weekdays(
	id serial not null primary key,
	weekDay text
);

insert into weekdays (id, weekDay) VALUES (1, Sunday);
insert into weekdays (id, weekDay) VALUES (2, Monday);
insert into weekdays (id, weekDay) VALUES (3, Tuesday);
insert into weekdays (id, weekDay) VALUES (4, Wednesday);
insert into weekdays (id, weekDay) VALUES (5, Thursday);
insert into weekdays (id, weekDay) VALUES (6, Friday);
insert into weekdays (id, weekDay) VALUES (7, Saturday);
