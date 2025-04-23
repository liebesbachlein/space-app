package app.dto.request;

import app.util.validation.PriceConstraint;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@GroupSequence({EditSpaceRequest.class, EditSpaceRequest.Default.class, EditSpaceRequest.Extended.class})
public class EditSpaceRequest {
    @NotBlank(message = "Name is a required field")
    @Size(min = 3, max=100, message = "Name must be of size 3 - 100")
    String name;

    @NotNull(message = "Type is a required field")
    @Min(value = 1, message = "Type id must be a positive nonzero integer")
    int typeId;

    @DecimalMax(value = "1000000.0",  message = "Price must be less than 1000000", groups = {Extended.class})
    @DecimalMin(value = "10.0", message = "Price must be more than 10", groups = {Extended.class})
    @PriceConstraint(message = "Price is a required field, must consist of numbers")
    BigDecimal price;

    public interface Default {}
    public interface Extended {}
}
