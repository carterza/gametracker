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
    
    public static Finder<Long,Game> find = new Finder<Long,Game>(Long.class, Game.class);
    
    public static List<Game> all() {
        return find.all();
    }
    
    public static List<Game> wanted() {
        return find.fetch("votes")
            .where()
            .eq("owned", false)
            .findList();
    }
    
    public static List<Game> wantedByVotesDesc() {
        List<Game> wantedGames = find.fetch("votes")
            .where()
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
    
    public static Game create(Game game) {
        game.save();
        return game;
    }
    
    public static void update(Long id, String title, boolean owned) {
        Game game = Game.find.ref(id);
        game.title = title;
        game.owned = owned;
        game.update();
    }
    
   static class VoteCountComparator implements Comparator<Game> {
        public int compare(Game g1, Game g2) {
            int numVotes1 = g1.votes.size(),
                numVotes2 = g2.votes.size();
            
            return (numVotes1 == numVotes2) ? 0
                : (numVotes1 > numVotes2) ? 1
                : -1;
        }
    }
}