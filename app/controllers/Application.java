package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import play.libs.Json;

import models.Game;

import views.html.*;

public class Application extends Controller {
  
  	public static Result index() {
    	return ok(index.render("Games", Game.all()));
  	}
    
    public static Result games() {
        return ok(Json.toJson(Game.wantedByVotesDesc()));
    }
    
    public static Result ownedGames() {
        return ok(Json.toJson(Game.owned()));
    }
	
	public static Result update(Long id) {
		Game.update(id, form().bindFromRequest().get("title"), Boolean.valueOf(form().bindFromRequest().get("owned")));
		return ok();
	}
	
	public static Result add() {
		Form<Game> gameForm = form(Game.class).bindFromRequest();
		if (gameForm.hasErrors()) {
			return badRequest();
		} else {
			return ok(
				Json.toJson(Game.create(gameForm.get()))
			);
		}
	}
  	
	public static Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(
            Routes.javascriptRouter("jsRoutes",
                controllers.routes.javascript.Application.games(),
                controllers.routes.javascript.Application.ownedGames(),
				controllers.routes.javascript.Application.add(),
				controllers.routes.javascript.Application.update()
			)
		);
	}
}