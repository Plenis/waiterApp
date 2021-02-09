package net.webapp;

import java.util.List;

public class Day {

    private Long id;
    private String dayName;
    private List<String> users;
    boolean isWorking = true;
    String selected;

    public Day() { }

    Day(Long id, String dayName){
        this.id = id;
        this.dayName = dayName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }


    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    @Override
    public String toString() {
        return "Day{" +
                "id=" + id +
                ", dayName='" + dayName + '\'' +
                ", users=" + users +
                '}';
    }
}
