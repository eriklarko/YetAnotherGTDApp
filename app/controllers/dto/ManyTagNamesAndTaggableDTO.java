package controllers.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Thinner
 */
public class ManyTagNamesAndTaggableDTO {

    @NotNull
    public Long taggableId;

    @NotNull
    @Size(min = 1)
    public List<String> tagNames = new ArrayList<>();
}
