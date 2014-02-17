package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import play.data.Form;

/**
 *
 * @author eriklark
 */
public class JsonUtil {

	public static String[] getTagNames(JsonNode tags) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(tags.traverse(), String[].class);
		} catch (NullPointerException | IOException ex) {
			return null;
		}
	}

	public static boolean onlyEmptyStrings(String[] strings) {
		for (String string : strings) {
			if (!(string == null || string.trim().isEmpty())) {
				return false;
			}
		}
		return true;
	}

    public static <T> Form<T> getFromJson(Class<T> clazz, JsonNode json) {
        Form<T> f = new Form(clazz);
        return f.bind(json);
    }

	private JsonUtil() {

	}
}
