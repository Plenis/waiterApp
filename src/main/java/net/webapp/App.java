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

                    System.out.println();
                    return  new ModelAndView(waiter, "register.handlebars");

                },new HandlebarsTemplateEngine()
        );

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

                },new HandlebarsTemplateEngine()
        );

        get("/waiters/:username", (request, response) -> {
            Map<String, Object> waiterMap = new HashMap<>();

            UserService userService = new UserService(jdbi);
            User user = userService.getOneUser(request.params("username"));

            List <Day> dayList = userService.dayList(); // all the days
            List <User> userDayList = userService.getDaysByUsername(request.params("username")); // days selected by user

            String firstname = request.queryParams("firstname");
            String lastname = request.queryParams("lastname");
            String username = request.params("username");

            System.out.println("userDayList: " + userDayList);
//            System.out.println("dayList: " + dayList.get(0).getDay_name());
            System.out.println("user: " + user);
            System.out.println(userService.getAllUsers());

            waiterMap.put("user", user);
            waiterMap.put("userDayList", userDayList);
            waiterMap.put("dayList", dayList);

            waiter.get(firstname);
            waiter.get(lastname);
            waiter.get(username);
//            waiter.get(dayList);
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
            UserService userService = new UserService(jdbi);
            User user = userService.getOneUser(request.params("username"));

            Set<String> daysList = request.queryParams();

            for (String  dayName: daysList){
                Day day = userService.getOneDay(dayName);
                userService.addUserDays(user.getId(), day.getId());
                waiter.put("weekday", dayName);
            }

            response.redirect("/waiters/" + request.params("username"));

            return new ModelAndView(waiter, "waiter.handlebars");

        }, new HandlebarsTemplateEngine());

        get("/days", (request, response) -> {
            Map<String, Object> shiftMap = new HashMap<>();

            UserService userService = new UserService(jdbi);

            List <Day> dayList = userService.dayList(); // all the days
            List <User> userDayList = userService.getDaysByUsername(request.params("username"));


            System.out.println("get route....");
            System.out.println(userDayList);
            System.out.println(dayList);

            shiftMap.put("dayList", dayList);
            shiftMap.put("userDayList", userDayList);
//            shiftMap.put("user", userService.getDaysByUsername("SPlenis"));

            return new ModelAndView(shiftMap, "admin.handlebars");

        }, new HandlebarsTemplateEngine());

        post("/days", (request, response) -> {
            Map<String, Object> shiftMap = new HashMap<>();

            UserService userService = new UserService(jdbi);

            List <Day> dayList = userService.dayList(); // all the days
            List <User> userDayList = userService.getDaysByUsername(request.params("username"));

            shiftMap.put("dayList", dayList);
            shiftMap.put("userDayList", userDayList);
            shiftMap.put("user", userService.getDaysByUsername(request.params("")));

            return new ModelAndView(shiftMap, "admin.handlebars");

        }, new HandlebarsTemplateEngine());

    }
//        catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }
}