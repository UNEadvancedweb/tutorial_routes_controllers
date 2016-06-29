package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    public Result index() {
        return ok("My my, I do believe it worked.");
    }

    public Result upper(String input) {
        return ok(input.toUpperCase());
    }

}
