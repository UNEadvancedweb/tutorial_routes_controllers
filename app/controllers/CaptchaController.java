package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import model.Captcha;


/**
 * This controller is designed to be very simple in terms of the Play elements it uses -- we're not using templates.
 * Just looking at requests and returning results.
 */
public class CaptchaController extends Controller  {

    /**
     * Controller action for showing the list of pictures
     * @return
     */
    public Result showPictures() {
        int arrayLength = 5;
        int[] indexes = new int[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            indexes[i] = (int)(Captcha.numPhotos() * Math.random());
        }

        StringBuilder sb = new StringBuilder();

        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n");
        sb.append("<style>img { max-width: 300px; }</style>\n");
        sb.append("<h2>Let's play spot the beagle!</h2>\n");



        sb.append("<form method='POST'>");
        sb.append("<ul>\n");

        for (int idx : indexes) {
            sb.append("<li>\n");
            sb.append(String.format("<img src='%s' />\n", Captcha.getPhoto(idx)));
            sb.append(String.format("<input type=\"checkbox\" name=\"beagle\" value=\"%d\" /> <label>It's a beagle!</label>", idx));
            sb.append(String.format("<input type=\"hidden\" name=\"sent\" value=\"%d\" /> <label>It's a beagle!</label>", idx));
            sb.append("</li>\n");
        }

        sb.append("</ul>\n");

        sb.append("<button type=\"submit\">Submit</input>");
        sb.append("</form>");

        return ok(sb.toString()).as("text/html");
    }

    private int countBeagles(String[] indexes) {
        int i = 0;
        for (String s : indexes) {
            if (Captcha.isCorrect(Integer.valueOf(s))) {
                i++;
            }
        }
        return i;
    }

    /**
     * Controller action for receiving the list of checked pictures
     */
    public Result matches() {
        String[] sent = request().body().asFormUrlEncoded().get("sent");
        String[] beagles = request().body().asFormUrlEncoded().get("beagle");

        int numBeagles = countBeagles(sent);
        int numFound = countBeagles(beagles);

        return ok(String.format("You found %d of %d beagles", numFound, numBeagles));
    }

    /**
     * A simple form example that accepts a GET request
     */
    public Result simpleForm(String query) {

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n");
        sb.append("<style>img { max-width: 300px; }</style>\n");
        sb.append("<h2>A trivial form!</h2>\n");
        sb.append("<form>\n");
        sb.append(String.format("<p>The query was %s</p>\n", query));
        sb.append("<p><input name='query' /><button type='submit'>Submit</button></p>\n");
        sb.append("</form>\n");


        return ok(sb.toString()).as("text/html");
    }

    public Result allPicturesAsJson() {

        ObjectNode obj = Json.newObject();
        ArrayNode arrayNode = Json.newArray();

        for (String url : Captcha.yesPhotos) {
            ObjectNode photo = Json.newObject();
            photo.put("url", url);
            photo.put("isBeagle", true);
            arrayNode.add(photo);
        }
        for (String url : Captcha.noPhotos) {
            ObjectNode photo = Json.newObject();
            photo.put("url", url);
            photo.put("isBeagle", false);
            arrayNode.add(photo);
        }
        obj.set("photos", arrayNode);

        return ok(obj);
    }


    public Result redirectTest() {
        return redirect(routes.CaptchaController.showPictures());
    }

}
