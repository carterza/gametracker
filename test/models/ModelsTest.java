package models;

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
		new Game(1L, "Call of Duty: Ghosts").save();
		Game callOfDutyGhosts = Game.find.byId(1L);
		assertNotNull(callOfDutyGhosts);
		assertEquals("Call of Duty: Ghosts", callOfDutyGhosts.title);
	}
}