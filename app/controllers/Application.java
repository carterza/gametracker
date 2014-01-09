package controllers;

import java.util.Map;
import java.util.Map.*;

import static play.data.Form.*;

import play.*;
import play.mvc.*;
import play.data.*;

import com.fasterxml.jackson.databind.node.ObjectNode;

import models.*;

import views.html.*;

public class Application extends Controller {
    
    public static Result index() {
        return ok(index.render());
    }
}