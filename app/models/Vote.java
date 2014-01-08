package models;

import java.util.*;

import play.data.validation.Constraints.*;

import play.db.ebean.*;
import javax.persistence.*;

/**
 * Vote entity managed by Ebean
 */
@Entity
public class Vote extends Model {

    private static final long serialVersionUID = 1L;
    
    @Id
    public Long id;    
    
    public static Finder<Long,Vote> find = new Finder<Long,Vote>(Long.class, Vote.class);
    
    public static List<Vote> all() {
        return find.all();
    }
    
    public static Vote create(Vote vote) {
        vote.save();
        return vote;
    }
}