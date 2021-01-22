package net.webapp;

public class Shift {
    Long id;
    Long userId;
    Long dayId;

    Shift(){}

    public Shift(Long id, Long userId, Long dayId) {
        this.id = id;
        this.userId = userId;
        this.dayId = dayId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDayId() {
        return dayId;
    }

    public void setDayId(Long dayId) {
        this.dayId = dayId;
    }

}
