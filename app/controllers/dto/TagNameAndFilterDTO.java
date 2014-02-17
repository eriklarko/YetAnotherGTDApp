package controllers.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Thinner
 */
public class TagNameAndFilterDTO {

    @NotNull
    public Long filterId;

    @NotNull
    @Size(min = 1)
    public String tagName;
}
