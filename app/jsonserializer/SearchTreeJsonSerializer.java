package jsonserializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import search.Node;
import search.SearchTreeToJson;

/**
 * Created with IntelliJ IDEA.
 * User: eriklark
 * Date: 10/10/13
 * Time: 2:16 PM
 */

public class SearchTreeJsonSerializer extends JsonSerializer<Node> {

    @Override
    public Class<Node> handledType() {
        return Node.class;
    }

    @Override
    public void serialize(Node value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeObject(SearchTreeToJson.parse(value));
    }
}
