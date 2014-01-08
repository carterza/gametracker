package controllers;

import java.util.Map;
import java.util.Map.*;

import static play.data.Form.*;

import play.*;
import play.mvc.*;
import play.data.*;

import play.libs.Json;
import play.libs.Json.*;                        
import static play.libs.Json.toJson;    
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.*;

import views.html.*;

public class Application extends Controller {
  
  	public static Result index() {
    	return ok(index.render("Games", Game.all()));
  	}
    
    public static Result games() {
        return ok(Json.toJson(Game.wantedByVotesDesc()));
    }
    
    public static Result votes() {
        return ok(Json.toJson(Vote.all()));
    }
    
    public static Result ownedGames() {
        return ok(Json.toJson(Game.owned()));
    }
	
	public static Result update(Long id) {
    DynamicForm f = form().bindFromRequest();
    Map<String, String> data = f.data();
    
    for(Entry<String, String> entry : data.entrySet()) {
      Logger.info(entry.getKey());
      Logger.info(entry.getValue());
    }
    // Game.update(id, form().bindFromRequest().get("title"), Boolean.valueOf(form().bindFromRequest().get("owned")));
		ObjectNode result = Json.newObject();
		result.put("status", "OK");
		return ok(result);
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
  
  public static Result addVote() {
    Form<Vote> voteForm = form(Vote.class).bindFromRequest();
    if(voteForm.hasErrors()) {
      return badRequest();
    } else {
      return ok(
        Json.toJson(Vote.create(voteForm.get()))
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
                controllers.routes.javascript.Application.update(),
                controllers.routes.javascript.Application.votes()
			)
		);
	}
}