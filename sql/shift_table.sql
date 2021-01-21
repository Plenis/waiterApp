create table shift(
	id serial not null primary key,
	user_id int not null,
	day_id int not null
);

select * from users join shift on users.id  = shift.user_id join week_day on week_day.id = shift.day_id where  username = :username;
