package controllers;

import play.mvc.*;
import play.data.Form;
import static play.data.Form.*;

import play.libs.Json;    
import static play.libs.Json.toJson;    

import java.util.*;

import models.*;

public class Votes extends Controller {
    
    public static Result index() {
        //return ok(Json.toJson(Vote.findByGame(game)));
        return ok();
    }
    
    public static Result add() {
        Vote newVote = Vote.create(Long.parseLong(form().bindFromRequest().get("game")));
        return ok(Json.toJson(newVote));
        // Form<Vote> voteForm = form(Vote.class).bindFromRequest();
        // if(voteForm.hasErrors()) {
            // return badRequest();
        // } else {
            // return ok(Json.toJson(Vote.create(voteForm.get(), game)));
        // }
        // return ok();
    }
}