package net.webapp;

public enum Weekdays {
    Sunday("Sunday"), Monday("Monday"), Tuesday("Tuesday"), Wednesday("Wednesday"), Thursday("Thursday"), Friday("Friday"), Saturday("Saturday");

    private final String weekDay;

    Weekdays(String weekDay) {

        this.weekDay = weekDay;
    }

    public String getWeekDay() {
        return weekDay;
    }
}
