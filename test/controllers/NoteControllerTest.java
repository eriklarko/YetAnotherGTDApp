package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

/**
 *
 * @author eriklark
 */
public class NoteControllerTest {

	@Test
	public void testAddNote() {
		NoteController.save();
	}

	@Test
	public void testListAll() throws JsonProcessingException {
		NoteController.list();
	}
}
