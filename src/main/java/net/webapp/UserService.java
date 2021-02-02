package net.webapp;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class UserService {
    private final Jdbi jdbi;
    List <User> userList;

    public UserService(Jdbi jdbi){
        this.jdbi = jdbi;
    }

    public List<User> getAllUsers(){

        String sql = "Select id, firstname, lastname, username, password from users";

        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .mapToBean(User.class)
                .list()
        );    }

    public User getOneUser(String user){
        String sql = "Select id, firstname, lastname, username, password from users where username = :username";

        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind("username", user)
                .mapToBean(User.class)
                .findOnly()
        );
    }

    public void insertNewUser(User user){
        String sql = "Insert into users " +
                "(firstname, lastname, username, password) " +
                "VALUES (?, ?, ?, ?)";
        jdbi.useHandle(handle -> handle.execute(sql,
                user.firstname,
                user.lastname,
                user.username,
                user.password
                )
        );
    }

    public List <Day> dayList(){
        String sql = "Select id, day_name from week_day";
        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .mapToBean(Day.class)
                .list()
        );
    }

    public Day getWorkingDay(String dayName){
        String sql = "Select id, day_name from week_day where day_name = ?";

        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind(0, dayName)
                .mapToBean(Day.class)
                .findOnly()
        );
    }

    public Shift deleteWorkingDays(Long userId){
        String sql = "delete from shift where user_id = ?";
        
        jdbi.useHandle(handle -> handle.execute(sql, userId));
        return null;
    }

    public void addUserDays(Long userId, Long dayId){
        String sql = "Insert into shift (user_id, day_id) VALUES (?, ?)";

        jdbi.useHandle(handle -> handle.execute(sql, userId, dayId)
        );
    }

    public List <User> getDaysByUsername(String name){
        String sql = "Select u.id u_id, u.firstname u_firstname, u.lastname u_lastname, u.username u_username, u.password u_password, " +
               "s.id s_id, s.user_id s_user_id, s.day_id s_day_id, " +
                 "d.id d_id, d.day_name d_day_name " +
                 "from users u " +
                "inner join shift s " +
                "on u.id = s.user_id " +
                "inner join week_day d " +
                "on d.id = s.day_id "+
                "where u.username = '" + name + "'";

        return jdbi.withHandle(handle -> {
                    userList = new ArrayList<>(handle.createQuery(sql)
                            .registerRowMapper(BeanMapper.factory(User.class, "u"))
                            .registerRowMapper(BeanMapper.factory(Day.class, "d"))
                            .registerRowMapper(BeanMapper.factory(Shift.class, "s"))
                            .reduceRows(new LinkedHashMap<Long, User>(),
                                    (map, rowView) -> {
                                        User user = map.computeIfAbsent(
                                                rowView.getColumn("u_id", Long.class),
                                                id -> rowView.getRow(User.class));

                                        if (rowView.getColumn("d_id", Long.class) != null) {
                                            user.addDay(rowView.getRow(Day.class));
                                        }
                                        return map;
                                    })
                            .values());
                    return userList;
                });

    }

    public List<String> getUsersByDay(Day dayName){
        String sql = "select users.username from users " +
                "join shift on users.id = shift.user_id " +
                "where shift.day_id = ?";

            return jdbi.withHandle(handle -> handle.createQuery(sql)
                    .bind(0, dayName.getId())
                    .mapTo(String.class)
                    .list()
            );
    }

    public Shift resetUsers(){
        String sql = "delete * from shift";

        jdbi.useHandle(handle -> handle.execute(sql)
        );
        return null;
    }

}
