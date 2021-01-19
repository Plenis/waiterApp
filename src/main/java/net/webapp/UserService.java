package net.webapp;

import org.jdbi.v3.core.Jdbi;

import java.lang.ref.SoftReference;
import java.util.List;

public class UserService {
    private Jdbi jdbi;

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
        String sql = "Insert into users (firstname, lastname, username, password) VALUES (?, ?, ?, ?)";
        jdbi.useHandle(handle -> handle.execute(sql,
                user.firstname,
                user.lastname,
                user.username,
                user.password
                )
        );
    }


}
