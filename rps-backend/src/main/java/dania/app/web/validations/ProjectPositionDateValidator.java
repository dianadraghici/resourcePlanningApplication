package dania.app.web.validations;
import dania.app.web.controllers.dto.ProjectPositionDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ProjectPositionDateValidator implements ConstraintValidator<ProjectPositionDateConstraints, ProjectPositionDTO> {

    @Override
    public void initialize(ProjectPositionDateConstraints projectDateConstraints) {
    }

    /*
     * returns true if:
     *  -   startDate for Project Position is before endDate for Project Position
     *  -   startDate for Project Position is before startDate for Project
     *  -   endDate for Project Position is after endDate for Project
     * */
    public boolean isValid(ProjectPositionDTO value, ConstraintValidatorContext context) {
        if (value.getProjectDTO() != null) {
            if (value.getProjectDTO().getStartDateCalendarDTO() != null) {
                LocalDate startDateProject = value.getProjectDTO().getStartDateCalendarDTO().getBop();
                if (value.getProjectDTO().getEndDateCalendarDTO() != null) {
                    LocalDate endDateProject = value.getProjectDTO().getEndDateCalendarDTO().getEop();
                    if (value.getStartDateCalendarDTO() != null) {
                        LocalDate startDate = value.getStartDateCalendarDTO().getBop();
                        if (value.getEndDateCalendarDTO() != null) {
                            LocalDate endDate = value.getEndDateCalendarDTO().getEop();
                            return startDate.compareTo(endDate) <= 0 &&
                                    startDateProject.compareTo(startDate) <= 0 &&
                                    endDateProject.compareTo(endDate) >= 0;
                        }
                    }
                }
            }
        }
        return true;
    }
}
