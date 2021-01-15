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

    public static void main(String[] args) {

        try {
            port(getHerokuAssignedPort());
        staticFiles.location("/public");

            Jdbi jdbi = getJdbiDatabaseConnection("jdbc:postgresql://localhost/waiters_app?username=sino&password=123");

            Map<String, Object> waiter = new HashMap<>();

        get("/", (request, response) -> new ModelAndView(waiter, "login.handlebars"), new HandlebarsTemplateEngine());

        get("/register", (request, response) -> new ModelAndView(waiter, "register.handlebars"), new HandlebarsTemplateEngine());


        get("/waiters/:username", (request, response) -> {
//            Map<String, Object> waiterMap = new HashMap<>();

            User user = new User(jdbi);

            String firstname = request.queryParams("firstname");
            String lastname = request.queryParams("lastname");
            String username = request.params("username");
            String weekday = request.queryParams("weekday");

            waiter.get(firstname);
            waiter.get(lastname);
            waiter.get(username);
            waiter.get(weekday);

            System.out.println(user.getAllUsers().size());
//            System.out.println("username" + username);
            UserLogin userLogin = user.getOneUser(username);
            System.out.println("name:" + userLogin.firstname);
            System.out.println("lastname" + userLogin.lastname);
            System.out.println("username" + userLogin.username);
            waiter.put("userLogin", userLogin);

            return new ModelAndView(user, "waiter.handlebars");

        }, new HandlebarsTemplateEngine());

        post("/waiters/:username", (request, response) -> {
            String firstname = request.queryParams("firstname");
            String lastname = request.queryParams("lastname");
            String username = request.queryParams("username");
//            String weekday = request.queryParams("weekday"); // one weekday selected


            ArrayList<Set<String>> shiftDay = new ArrayList<>();
            shiftDay.add(request.queryParams());

            System.out.println("firstname: " + firstname);
            System.out.println("lastname: "+ lastname);
            System.out.println("username: " + username);
            System.out.println("shiftDay: " + request.queryParams() + " | " + request.body());

            waiter.put("firstname", firstname);
            waiter.put("lastname" ,lastname);
            waiter.put("username", username);
            waiter.put("weekday", shiftDay);

            return new ModelAndView(waiter, "waiter.handlebars");

        }, new HandlebarsTemplateEngine());

        get("/days", (request, response) -> {
            Map<String, Object> map = new HashMap<>();
            String weekday = request.queryParams("weekday");

            waiter.put("weekday", weekday);
            return new ModelAndView(map, "waiter.handlebars");

        }, new HandlebarsTemplateEngine());

        get("/admin", ((request, response) ->
                /*
                 * According to the days selected by the waiter, the waiter name should appear
                 * under the days selected to work
                 *
                 * Admin should be able to change waiter working days
                 * */
                new ModelAndView(waiter, "admin.handlebars")), new HandlebarsTemplateEngine());

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
