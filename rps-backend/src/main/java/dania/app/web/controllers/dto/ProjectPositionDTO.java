package dania.app.web.controllers.dto;

import dania.app.web.validations.ProjectPositionDateConstraints;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ProjectPositionDateConstraints.List({
        @ProjectPositionDateConstraints(
                field = "startDateCalendarDTO",
                fieldMatch = "endDateCalendarDTO",
                message = "Project Position - Start Date cannot be after End Date!"
        )
})
public class ProjectPositionDTO {

    private Integer id;

    @NotNull(message = "Project Position - Position Id cannot be null")
    private Integer positionId;

    @NotNull(message = "Project Position - Number Positions cannot be null")
    private Integer numberPositions;

    @NotNull(message = "Project Position - Percent Id cannot be null")
    private Integer percentId;

    @NotNull(message = "Project Position - Project cannot be null")
    private ProjectDTO projectDTO;

    @NotNull(message = "Project Position - Start Date cannot be null")
    private CalendarDTO startDateCalendarDTO;

    @NotNull(message = "Project Position - End Date cannot be null")
    private CalendarDTO endDateCalendarDTO;

    @NotNull(message = "Project Position - Position Description cannot be null")
    private String positionDescription;

    @NotNull(message = "Project Position - Percent Description cannot be null")
    private String percentDescription;

    public String getProjectName() {
        return this.getProjectDTO().getProjectName();

    }

}
