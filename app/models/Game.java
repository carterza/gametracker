package models;

import javax.persistence.*;
import play.db.ebean.*;
import com.avaje.ebean.*;

@Entity
public class Game extends Model {
	
	@Id
	public Long id;
	public String title;
	
	public Game(Long id, String title) {
		this.id = id;
		this.title = title;
	}
	
	public static Finder<Long,Game> find = new Finder<Long,Game>(
		Long.class, Game.class
	);
}