package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import models.Note;
import models.Tag;
import org.junit.Test;
import utils.PlayIntegrationTest;
import utils.TestUtils;

/**
 *
 * @author eriklark
 */
public class NoteServiceTest extends PlayIntegrationTest {

	@Test
	public void testReplaceTags() {
		Note n = NoteService.instance().create("hej", Arrays.asList(TagService.createNewTagFromName("asd")));

		List<Tag> tags = Arrays.asList(
				TagService.createNewTagFromName("a"),
				TagService.createNewTagFromName("b"),
				TagService.createNewTagFromName("c")
		);
		NoteService.instance().replaceTags(n, tags);
		n = NoteService.instance().byId(n.id);

		TestUtils.assertListEquals(tags, new ArrayList<>(n.tags));
	}
}
