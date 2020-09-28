package net.waiter;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
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

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        staticFiles.location("/public");

        get("/", (request, response) -> {
            Map<String, Object> map = new HashMap<>();
            return new ModelAndView(map, "login.handlebars");

        }, new HandlebarsTemplateEngine());

        get("/register", (request, response) -> {
            Map<String, Object> map = new HashMap<>();
            return new ModelAndView(map, "register.handlebars");

        }, new HandlebarsTemplateEngine());

        get("/waiter", (request, response) -> {
            Map<String, Object> map = new HashMap<>();
            return new ModelAndView(map, "waiter.handlebars");

        }, new HandlebarsTemplateEngine());



//        post("/waiter:days", (request, response) -> {
//            Map<String, Object> map = new HashMap<>();
//            return new ModelAndView(map, "waiter.handlebars");
//        }, new HandlebarsTemplateEngine());

    }
}
