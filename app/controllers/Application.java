package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    public static Result index() {
        return ok("My my, I do believe it worked.");
    }

    public static Result upper(String input) {
        return ok(input.toUpperCase());
    }

}
