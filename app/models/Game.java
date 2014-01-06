package models;

import java.util.*;

import javax.persistence.*;
import play.db.ebean.*;
import com.avaje.ebean.*;

/**
 * Game entity managed by Ebean
 */
@Entity
public class Game extends Model {

    private static final long serialVersionUID = 1L;
    
    @Id
    public Long id;
    
    public String title;
    
    public boolean owned = false;
    
    @OneToMany(cascade=CascadeType.ALL)
    public List<Vote> votes;
    
    public Game(String title, boolean owned) {
        this.title = title;
        this.owned = owned;
        this.votes = new ArrayList<Vote>();
    }
    
    public static Finder<Long,Game> find = new Finder<Long,Game>(Long.class, Game.class);
    
    public static List<Game> findWantedByVotesDesc() {
        List<Game> wantedGames = find.fetch("votes")
            .where()
            .eq("owned", false)
            .findList();
        Collections.sort(wantedGames, Collections.reverseOrder(new Game.VoteCountComparator()));
        return wantedGames;
    }
    
    public static List<Game> findOwnedByTitleAsc() {
        return find.where()
                .eq("owned", true)
                .orderBy("title asc")
                .findList();
    }
    
    /**
    * Create a new game
    */
    public static Game create(String title, boolean owned, String creator) {
        Game game = new Game(title, owned);
        game.votes.add(new Vote(game, User.findByEmail(creator)));
        game.save();
        return game;
    }
        
    public String toString() {
        return "Game(" + id + ")";
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