package controllers;

import java.util.*;

import static play.data.Form.form;
import play.mvc.*;
import views.html.games.*;
import models.*;

@Security.Authenticated(Secured.class)
public class Games extends Controller {
    
    public static Result add() {
        Game newGame = Game.create(
                "Hello World!",
                false,
                request().username()
        );
        return ok(wantedGames.render(Game.findWantedByVotesDesc()));
    }
    
}