package net.webapp;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void canAddUser() {
    User user = new User();
    Day day = new Day();
    user. setFirstName("Sino");
    user. setLastName("Plenis");
    user. setUsername("SPlenis");
    user. setPassword("123sp");
    user.addDay(day);

    assertEquals("SPlenis", user.getUsername());
    }
}