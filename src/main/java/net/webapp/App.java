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

        get("/", (request, response) -> {

            return new ModelAndView(waiter, "login.handlebars");

        }, new HandlebarsTemplateEngine());

        get("/register", (request, response) -> {

            return new ModelAndView(waiter, "register.handlebars");

        }, new HandlebarsTemplateEngine());


        get("/waiters/:username", (request, response) -> {

            String firstName = request.queryParams("firstname");
            String lastName = request.queryParams("lastname");
            String username = request.queryParams("username");
            String weekday = request.queryParams("weekday");

            waiter.get(firstName);
            waiter.get(lastName);
            waiter.get(username);
            waiter.get(weekday);
            return new ModelAndView(waiter, "waiter.handlebars");

        }, new HandlebarsTemplateEngine());

        post("/waiters/:username", (request, response) -> {
            String firstname = request.queryParams("firstname");
            String lastname = request.queryParams("lastname");
            String username = request.queryParams("username");
            String weekday = request.queryParams("weekday");


            ArrayList<Set<String>> shiftDay = new ArrayList<>();
            shiftDay.add(request.queryParams());

            System.out.println(weekday);
            System.out.println("------");
            System.out.println(request.queryParams() + " | " + request.body());

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
                new ModelAndView(waiter, "admin.handlebars")), new HandlebarsTemplateEngine());

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
