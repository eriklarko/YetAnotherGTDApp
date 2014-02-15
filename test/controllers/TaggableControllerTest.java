package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

/**
 *
 * @author eriklark
 */
public class TaggableControllerTest {

	@Test
	public void testAddTaggable() {
		TaggableController.save();
	}

	@Test
	public void testListAll() throws JsonProcessingException {
		TaggableController.list();
	}
}
