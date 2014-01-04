package models;

import java.util.List;

import javax.persistence.*;
import play.db.ebean.*;
import com.avaje.ebean.*;

@Entity
public class Game extends Model {
	
	@Id
	public Long id;
	public String title;
	public Boolean owned;
	
	public Game(Long id, String title, Boolean owned) {
		this.id = id;
		this.title = title;
		this.owned = owned;
	}
	
	public static Finder<Long,Game> find = new Finder<Long,Game>(
		Long.class, Game.class
	);
	
	public static List<Game> findOwned() {
		return find.where()
				.eq("owned", true)
				.findList();
	}
}