package net.waiter;

import org.jdbi.v3.core.Jdbi;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

    static Jdbi getJdbiDatabaseConnection(String defualtJdbcUrl) throws URISyntaxException, SQLException {
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

        return Jdbi.create(defualtJdbcUrl);

    }

    public static void main(String[] args) {
        try {

            port(getHerokuAssignedPort());
        staticFiles.location("/public");

            Jdbi jdbi = getJdbiDatabaseConnection("jdbc:postgresql://localhost/waiter_app?username=sino&password=123");

            Map<String, Object> waiter = new HashMap<>();

        get("/", (request, response) -> {

            Map<String, Object> map = new HashMap<>();
            return new ModelAndView(map, "login.handlebars");

        }, new HandlebarsTemplateEngine());

        get("/waiters/:username", (request, response) -> {
            String firstName = request.queryParams("firstName");
            String lastName = request.queryParams("lastName");
            String username = request.queryParams("username");

            waiter.get(firstName);
            waiter.get(lastName);
            waiter.get(username);

            return new ModelAndView(waiter, "waiter.handlebars");

        }, new HandlebarsTemplateEngine());

        post("/waiters/:username", (request, response) -> {

            String firstName = request.queryParams("firstName");
            String lastName = request.queryParams("lastName");
            String username = request.queryParams("username");

            waiter.put(firstName, "firstName");
            waiter.put(lastName, "lastName");
            waiter.put(username, "username");

            return new ModelAndView(waiter, "waiter.handlebars");

        }, new HandlebarsTemplateEngine());

        get("/days", (request, response) -> {
            Map<String, Object> map = new HashMap<>();
            return new ModelAndView(map, "admin.handlebars");

        }, new HandlebarsTemplateEngine());

        get("/register", (request, response) -> {
            Map<String, Object> map = new HashMap<>();
            return new ModelAndView(map, "register.handlebars");

        }, new HandlebarsTemplateEngine());
//
//        get("/waiter", (request, response) -> {
//
//            List<UserLogin> userLogins = jdbi.withHandle((h) -> {
//                List<UserLogin> userLoginList = h.createQuery("select firstname, lastname, username from users")
//                        .mapToBean(UserLogin.class)
//                        .list();
//                return userLoginList;
//            });
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("userLogins", userLogins);
//            return new ModelAndView(map, "waiter.handlebars");
//
//        }, new HandlebarsTemplateEngine());
//
//
//        post("/waiter", (request, response) -> {
//
//            String username = request.queryParams("username");
//
//            jdbi.useHandle(h -> {
//                h.execute("insert into users (firstname, lastname, username, password) values (?, ?, ?)",
//                        username);
//            });
//
//            response.redirect("/waiter");
//            return "";
//        });

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
