package net.webapp;

import org.jdbi.v3.core.Jdbi;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.net.URI;
import java.net.URISyntaxException;
//import java.sql.SQLException;
import java.util.*;
//import java.util.List;

import static spark.Spark.*;

public class App {

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

    static Jdbi getJdbiDatabaseConnection(String defaultJdbcUrl) throws URISyntaxException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String database_url = processBuilder.environment().get("DATABASE_URL");
        if (database_url != null) {

            URI uri = new URI(database_url);
            String[] hostParts = uri.getUserInfo().split(":");
            String username = hostParts[0];
            String password = hostParts[1];
            String host = uri.getHost();

            int port = uri.getPort();

            String path = uri.getPath();
            String url = String.format("jdbc:postgresql://%s:%s%s", host, port, path);

            return Jdbi.create(url, username, password);
        }

        return Jdbi.create(defaultJdbcUrl);
    }

    public static void main(String[] args) throws URISyntaxException {

//        try {
        port(getHerokuAssignedPort());
        staticFiles.location("/public");

        Jdbi jdbi = getJdbiDatabaseConnection("jdbc:postgresql://localhost/waiters_app?username=sino&password=123");

        Map<String, Object> waiter = new HashMap<>();


        get("/", (request, response) -> new ModelAndView(waiter, "login.handlebars"), new HandlebarsTemplateEngine());

        get("/register", (request, response) -> {

                    return  new ModelAndView(waiter, "register.handlebars");

                },new HandlebarsTemplateEngine());

        post("/register", (request, response) -> {

                    String firstname = request.queryParams("firstname");
                    String lastname = request.queryParams("lastname");
                    String username = request.queryParams("username");
                    String password = request.queryParams("password");

                    User user = new User();
                    user.setFirstName(firstname);
                    user.setLastName(lastname);
                    user.setUsername(username);
                    user.setPassword(password);

                    UserService userService = new UserService(jdbi);
                    userService.insertNewUser(user);

                    response.redirect("/waiters/" + username);

                    return  new ModelAndView(waiter, "register.handlebars");

                },new HandlebarsTemplateEngine());

        get("/waiters/:username", (request, response) -> {
            Map<String, Object> waiterMap = new HashMap<>();

            UserService userService = new UserService(jdbi);
            User user = userService.getOneUser(request.params("username"));
            Day dayWorking = new Day();
//            Boolean isWorkingDay = dayWorking.isWorking();

            //            List<User> allUsers = userService.getAllUsers();
            List <Day> dayList = userService.dayList(); // all the days
            List <User> userDayList = userService.getDaysByUsername(request.params("username")); // days selected by user

//            for (Day isWorkingDay: dayList){
//                isWorkingDay.setWorking(false);
//
//                System.out.println("isWorkingDay: " + isWorkingDay);
//            }

            String firstname = request.queryParams("firstname");
            String lastname = request.queryParams("lastname");
            String username = request.params("username");

            waiterMap.put("user", user);
            waiterMap.put("userDayList", userDayList);
            waiterMap.put("dayList", dayList);

            waiter.get(firstname);
            waiter.get(lastname);
            waiter.get(username);
            return new ModelAndView(waiterMap, "waiter.handlebars");

        }, new HandlebarsTemplateEngine());

        post("/waiters/:username",(request, response) -> {

            String firstname = request.queryParams("firstname");
            String lastname = request.queryParams("lastname");
            String username = request.queryParams("username");

            waiter.put("firstname", firstname);
            waiter.put("lastname" ,lastname);
            waiter.put("username", username);

            response.redirect("/waiters/" + username);


            return new ModelAndView(waiter, "waiter.handlebars");

        }, new HandlebarsTemplateEngine());

        post("/waiters/:username/confirm",(request, response) -> {
            String message = null;
            UserService userService = new UserService(jdbi);
            User user = userService.getOneUser(request.params("username"));

            Set<String> daysList = request.queryParams();

            if (daysList.size() == 3){
                for (String dayName: daysList) {
                    Day day = userService.getWorkingDay(dayName);
                    userService.addUserDays(user.getId(), day.getId());
                    waiter.put("weekday", dayName);
                }
            }
            else{
                message = "User selected more than 3 days";
                System.out.println("You selected more / less than 3 days");
            }
            
            waiter.get(message);

            response.redirect("/waiters/" + request.params("username"));

            return new ModelAndView(waiter, "waiter.handlebars");

        }, new HandlebarsTemplateEngine());

        get("/waiters/:username/update",(request, response) -> {
            Map<String, Object> userMap = new HashMap<>();
            String username = request.params("username");

            UserService userService = new UserService(jdbi);
            User user = userService.getOneUser(username);
            Shift deleteUserDays = userService.deleteWorkingDays(user.getId());

            userMap.put("user", deleteUserDays);

            response.redirect("/waiters/" + username);

            return new ModelAndView(userMap, "waiter.handlebars");

        }, new HandlebarsTemplateEngine());

        post("/waiters/:username/edit",(request, response) -> {
            Map<String, Object> userMap = new HashMap<>();
            String username = request.params("username");

            UserService userService = new UserService(jdbi);
            User user = userService.getOneUser(username);
//            Shift deleteUserDays = userService.deleteWorkingDays(user.getId());

//            userMap.put("user", deleteUserDays);

            response.redirect("/waiters/" + username + "/edit");

            return new ModelAndView(userMap, "edit.handlebars");

        }, new HandlebarsTemplateEngine());

        get("/days", (request, response) -> {
            Map<String, Object> shiftMap = new HashMap<>();

            UserService userService = new UserService(jdbi);

            List <Day> dayList = userService.dayList(); // all the days

            for (Day shiftDayName: dayList){
                List <String> users = userService.getUsersByDay(shiftDayName);
                shiftDayName.setUsers(users);
            }

//            System.out.println(dayList);
            shiftMap.put("dayList", dayList);

            return new ModelAndView(shiftMap, "admin.handlebars");

        }, new HandlebarsTemplateEngine());

        post("/days", (request, response) -> {
            Map<String, Object> shiftMap = new HashMap<>();

            UserService userService = new UserService(jdbi);

            List <Day> dayList = userService.dayList(); // all the days
            List <User> userDayList = userService.getDaysByUsername(request.params("username"));

            shiftMap.put("dayList", dayList);
            shiftMap.put("userDayList", userDayList);
            return new ModelAndView(shiftMap, "admin.handlebars");

        }, new HandlebarsTemplateEngine());

        get("/days/reset",(request, response) -> {
            Map<String, Object> userMap = new HashMap<>();
            UserService userService = new UserService(jdbi);
            Shift resetShifts = userService.resetUsers();

            userMap.put("user", resetShifts);

            response.redirect("/days");

            return new ModelAndView(userMap, "admin.handlebars");

        }, new HandlebarsTemplateEngine());

        get("/days/confirm", (request, response) -> {
            Map<String, Object> shiftMap = new HashMap<>();

            UserService userService = new UserService(jdbi);

            List <Day> dayList = userService.dayList(); // all the days
            List <User> userDayList = userService.getDaysByUsername(request.params("username"));

            shiftMap.put("dayList", dayList);
            shiftMap.put("userDayList", userDayList);
            shiftMap.put("user", userService.getDaysByUsername(request.params("")));

            return new ModelAndView(shiftMap, "admin.handlebars");

        }, new HandlebarsTemplateEngine());

        get("/all_waiters", ((request, response) -> {
            Map <String, Object> usersMap = new HashMap<>();
            UserService userService = new UserService(jdbi);
            List <User> users = userService.getAllUsers();

            System.out.println(users);

//            waiter.get(user);
            usersMap.put("user", users);
            return new ModelAndView(usersMap, "user.handlebars");
        }), new HandlebarsTemplateEngine());
    }
//        catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }
}