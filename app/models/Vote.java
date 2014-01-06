package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

/**
* Task entity managed by Ebean
*/
@Entity
public class Vote extends Model {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    public Long id;
    
    @ManyToOne
    public User voter;
    
    @ManyToOne(cascade=CascadeType.ALL)
    public Game game;
    
    public Vote(Game game, User voter) {
        this.game = game;
        this.voter = voter;
    }
    
    public static Model.Finder<Long,Vote> find = new Model.Finder<Long,Vote>(Long.class, Vote.class);
    
    public static List<Vote> findByGame(Long game) {
        return Vote.find.where()
            .eq("game.id", game)
            .findList();
    }
    
    public static Vote create(Vote vote, Long user, Long game) {
        vote.voter = User.find.ref(user);
        vote.game = Game.find.ref(game);
        vote.save();
        return vote;
    }
    
    public String toString() {
        return "Vote(" + id + ") for game " + game;
    }
}