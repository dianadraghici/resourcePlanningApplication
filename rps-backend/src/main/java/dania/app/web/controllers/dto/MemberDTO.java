package dania.app.web.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private Integer id;

    @NotNull(message = "Stuff number cannot be null")
    @Size(min = 1, message = "Stuff number length should be minium 1 character!")
    private String staffNumber;

    @NotNull(message = "Last Name cannot be null")
    @Size(min = 1, message = "Last Name length should be minium 1 character!")
    private String lastName;

    @NotNull(message = "First Name cannot be null")
    @Size(min = 1, message = "First Name length should be minium 1 character!")
    private String firstName;

    @NotNull(message = "Flag cannot be null")
    private Byte flag;

    @NotNull(message = "Technology Id cannot be null")
    private Integer technologyId;

    @NotNull(message = "Technology Description cannot be null")
    @Size(min = 1, message = "Technology Description length should be minium 1 character!")
    private String technologyDescription;

    private String comment;

}
