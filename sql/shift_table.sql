create table shift(
	id serial not null primary key,
	user_id int not null,
	day_id int not null,
	foreign key (user_id) references users(id),
	foreign key (day_id) references week_day(id)
);
