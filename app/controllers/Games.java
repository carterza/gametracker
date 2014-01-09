package controllers;

import java.util.*;

import static play.data.Form.*;
import static play.libs.Json.toJson;

import play.mvc.*;
import play.data.Form;
import play.libs.Json;

import models.*;

public class Games extends Controller {
    
    public static Result index() {
        // if(owned)
            // return ok(Json.toJson(Game.owned()));
        // else
            // return ok(Json.toJson(Game.wantedByVotesDesc()));
        Form<Game> gameForm = form(Game.class).bindFromRequest();
        // if(gameForm.get("id"))
            // return ok(Json.toJson(Game.find(gameForm.get("id")))
        // else
            return ok(Json.toJson(Game.all()));
    }
    
    public static Result add() {
        Game newGame = Game.create(form().bindFromRequest().get("title"));
        return ok(Json.toJson(newGame));
    }
    
    public static Result update(Long game) {
        Game.markAsOwned(
            game,
            Boolean.valueOf(
                form().bindFromRequest().get("owned")
            )
        );
		return ok(Json.newObject().put("status", "OK"));
    }
}