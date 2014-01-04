package models;

import java.util.List;

import models.*;
import org.junit.*;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;

public class ModelsTest extends WithApplication {
	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase()));
	}
	
	@Test
	public void createAndRetrieveGame() {
		new Game(1L, "Call of Duty: Ghosts", false).save();
		Game callOfDutyGhosts = Game.find.byId(1L);
		assertNotNull(callOfDutyGhosts);
		assertEquals("Call of Duty: Ghosts", callOfDutyGhosts.title);
	}
	
	@Test
	public void findOwnedGame() {
		new Game(1L, "GTAV", false).save();
		new Game(2L, "Minecraft", true).save();
		List<Game> ownedGames = Game.findOwned();
		assertNotNull(ownedGames);
		assertEquals(1, ownedGames.size());
		assertEquals("Minecraft", ownedGames.get(0).title);
	}
}