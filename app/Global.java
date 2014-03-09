import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import play.Application;
import play.GlobalSettings;
import play.libs.Json;
import utils.Constants;

import java.text.SimpleDateFormat;
import jsonserializer.SearchTreeJsonSerializer;

/**
 * Created with IntelliJ IDEA.
 * User: eriklark
 * Date: 10/7/13
 * Time: 9:52 AM
 */

public class Global extends GlobalSettings {
    @Override
    public void onStart(Application app) {
        super.onStart(app);

        setupObjectMapper();
    }

    private void setupObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.setDateFormat(new SimpleDateFormat(Constants.SHORT_TIME_PATTERN));

        SimpleModule customSerializers = new SimpleModule("TheEpicCowOfDoom", new Version(1, 0, 0, null, null, null));
        customSerializers.addSerializer(new SearchTreeJsonSerializer());
        om.registerModule(customSerializers);

        Json.setObjectMapper(om);
    }
}
