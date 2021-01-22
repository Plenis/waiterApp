package net.webapp;

import java.util.ArrayList;
import java.util.List;

public class User {
    Long id;
    String firstname;
    String lastname;
    String username;
    String password;
    List <Day> listOfDays = new ArrayList<>();


    public User() { }

    public User(Long id, String firstname, String lastname, String username, String password){
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
    }

    public void addDay(Day day){
        listOfDays.add(day);
    }

    public List<Day> getListOfDays() {
        return listOfDays;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", listOfDays=" + listOfDays +
                '}';
    }
}
