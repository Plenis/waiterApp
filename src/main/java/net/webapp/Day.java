package net.webapp;

public class Day {

    private int id;
    private String day_name;

    public Day() {

    }

    Day(int id, String day_name){
        this.id = id;
        this.day_name = day_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay_name() {
        return day_name;
    }

    public void setDay_name(String day_name) {
        this.day_name = day_name;
    }
}
