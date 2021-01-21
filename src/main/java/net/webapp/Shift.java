package net.webapp;

public class Shift {
    int id;
    int userId;
    int dayId;

    Shift(){}

    public Shift(int id, int userId, int dayId) {
        this.id = id;
        this.userId = userId;
        this.dayId = dayId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }
}
