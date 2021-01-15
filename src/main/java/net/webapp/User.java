package net.webapp;

import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class User {
    private Jdbi jdbi;

    public User(Jdbi jdbi){
        this.jdbi = jdbi;
    }

    public List<UserLogin> getAllUsers(){

        String sql = "Select id, firstname, lastname, username, password from users";

        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .mapToBean(UserLogin.class)
                .list()
        );    }

    public UserLogin getOneUser(String user){
        String sql = "Select id, firstname, lastname, username, password from users where username = :username";

        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind("username", user)
                .mapToBean(UserLogin.class)
                .findOnly()
        );
    }

}
