package dania.app.web.controllers.dto;

import dania.app.web.validations.MemberPositionDateConstraints;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MemberPositionDateConstraints.List({
        @MemberPositionDateConstraints(
                field = "startDateCalendarDTO",
                fieldMatch = "endDateCalendarDTO",
                message = "Member Position - Start Date cannot be after End Date!"
        )
})
public class MemberPositionDTO {

    private Integer id;

    @NotNull(message = "Member Position - Member Position Percent Id cannot be null")
    private Integer percentId;

    @NotNull(message = "Member Position - Project Position cannot be null")
    private ProjectPositionDTO projectPositionDTO;

    @NotNull(message = "Member Position - Member cannot be null")
    private MemberDTO memberDTO;

    @NotNull(message = "Member Position - Start Date cannot be null")
    private CalendarDTO startDateCalendarDTO;

    @NotNull(message = "Member Position - End Date cannot be null")
    private CalendarDTO endDateCalendarDTO;

    @NotNull(message = "Member Position - Percent Description cannot be null")
    private String percentDescription;

}
