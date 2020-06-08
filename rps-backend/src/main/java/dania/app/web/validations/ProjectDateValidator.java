package dania.app.web.validations;

import dania.app.web.controllers.dto.ProjectDTO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ProjectDateValidator implements ConstraintValidator<ProjectDateConstraints, ProjectDTO> {

    @Override
    public void initialize(ProjectDateConstraints projectDateConstraints) {
    }

    /*
    * returns true if startDate is before than endDate
    * */
    public boolean isValid(ProjectDTO value, ConstraintValidatorContext context) {

        if (value.getStartDateCalendarDTO() != null) {
            LocalDate sDate = value.getStartDateCalendarDTO().getBop();
            if (value.getEndDateCalendarDTO() != null) {
                LocalDate eDate = value.getEndDateCalendarDTO().getEop();
                return sDate.compareTo(eDate) < 0;
            }
        }
        return true;
    }
}