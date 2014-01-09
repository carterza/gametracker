package models;

import java.util.*;

import play.data.validation.Constraints.*;

import play.db.ebean.*;
import javax.persistence.*;

/**
 * Game entity managed by Ebean
 */
@Entity
public class Game extends Model {

    private static final long serialVersionUID = 1L;
    
    @Id
    public Long id;
    
    @Required
    public String title;
    
    @Required
    public boolean owned;
    
    @OneToMany(cascade=CascadeType.ALL)
    public List<Vote> votes;
    
    public Game(String title) {
        this.title = title;
        this.owned = false;
    }
    
    public static Finder<Long,Game> find = new Finder<Long,Game>(Long.class, Game.class);
    
    public static List<Game> all() {
        return find.all();
    }
    
    public static List<Game> wanted() {
        return find.where()
            .eq("owned", false)
            .findList();
    }
    
    public static List<Game> wantedByVotesDesc() {
        List<Game> wantedGames = find.where()
            .eq("owned", false)
            .findList();
            
        Collections.sort(wantedGames, Collections.reverseOrder(new Game.VoteCountComparator()));
        
        return wantedGames;
    }
    
    public static List<Game> owned() {
        return find.where()
            .eq("owned", true)
            .orderBy("title asc")
            .findList();
    }
    
    public static Game create(String title) {
        Game game = new Game(title);
        game.save();
        return game;
    }
    
    public static void markAsOwned(Long gameId, Boolean owned) {
        Game game = Game.find.ref(gameId);
        game.owned = owned;
        game.update();
    }
    
    static class VoteCountComparator implements Comparator<Game> {
        public int compare(Game g1, Game g2) {
            int numVotes1 = Vote.findByGame(g1.id).size(),
                numVotes2 = Vote.findByGame(g2.id).size();
            
            return (numVotes1 == numVotes2) ? 0
                : (numVotes1 > numVotes2) ? 1
                : -1;
        }
    }
}