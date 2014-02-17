package controllers.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Thinner
 */
public class ManyTagNamesAndPayloadDTO {

    @NotNull
    @Size(min = 1)
    public String payload;

    @NotNull
    @Size(min = 1)
    public List<String> tagNames = new ArrayList<>();
}
