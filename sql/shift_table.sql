create table shift(
	id serial not null primary key,
	user_id int not null,
	day_id int not null
);

// alll user deets and days working
select * from users join shift on users.id  = shift.user_id join week_day on week_day.id = shift.day_id where  username = :username;

// all users working on a specific day
select users.username from users join shift on users.id  = shift.user_id join week_day on week_day.id = shift.day_id where day_name = 'Monday';

//days working for a specific user
select day_name
from week_day As wd
inner join shift As s
on wd.id = s.day_id
inner join users As u
on s.user_id = u.id
where u.id = 22;