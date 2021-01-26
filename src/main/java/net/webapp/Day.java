package net.webapp;

public class Day {

    private Long id;
    private String day_name;
    private Long user_id;
    private Boolean workingDay;

    public Day() { }

    Day(Long id, String day_name){
        this.id = id;
        this.day_name = day_name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDay_name() {
        return day_name;
    }

    public void setDay_name(String day_name) {
        this.day_name = day_name;
    }

    @Override
    public String toString() {
        return "Day{" +
                "id=" + id +
                ", day_name='" + day_name + '\'' +
                '}';
    }
}
