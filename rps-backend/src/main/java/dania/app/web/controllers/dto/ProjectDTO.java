package dania.app.web.controllers.dto;

import dania.app.web.validations.ProjectDateConstraints;
import dania.app.web.validations.StatusConstraints;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@StatusConstraints.List({
        @StatusConstraints(
                field = "statusId",
                fieldMatch = "percentId",
                message = "Project Status is not applicable to Project Certitude!"
        )
})
@ProjectDateConstraints.List({
        @ProjectDateConstraints(
                field = "startDateCalendarDTO",
                fieldMatch = "endDateCalendarDTO",
                message = "Project - Start Date cannot be after End Date!"
        )
})
public class ProjectDTO {

    private Integer id;

    @NotNull(message = "Project Code cannot be null")
    @Size(min = 1, message = "Project Code length should be minium 1 character!")
    private String projectCode;

    @NotNull(message = "Project Name cannot be null")
    @Size(min = 1, message = "Project Name length should be minium 1 character!")
    private String projectName;

    @NotNull(message = "Status Id cannot be null")
    private Integer statusId;

    @NotNull(message = "Percent Id cannot be null")
    private Integer percentId;

    @NotNull(message = "Status Description cannot be null")
    private String statusDescription;

    @NotNull(message = "Percent Description cannot be null")
    private String percentDescription;

    @NotNull(message = "Start Date cannot be null")
    private CalendarDTO startDateCalendarDTO;

    @NotNull(message = "End Date cannot be null")
    private CalendarDTO endDateCalendarDTO;

}
