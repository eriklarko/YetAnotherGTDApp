package controllers.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Thinner
 */
public class TagNameAndTaggableDTO {

    @NotNull
    public Long taggableId;

    @NotNull
    @Size(min = 1)
    public String tagName;
}
