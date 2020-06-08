package dania.app.web.validations;

import dania.app.web.controllers.dto.ProjectDTO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StatusValidator implements ConstraintValidator<StatusConstraints, ProjectDTO> {

    @Override
    public void initialize(StatusConstraints statusConstraints) {
    }

    /*
     * returns true if status Project is applicable to certitude Project
     * */
    public boolean isValid(ProjectDTO value, ConstraintValidatorContext context) {

        if (value.getPercentId() != null) {
            int percentId = value.getPercentId();
            if (value.getStatusId() != null) {
                int statusId = value.getStatusId();


                return ((percentId == 0 && statusId == 0) ||                        // for Certitude = 0% -> Status = 'ABANDONED'
                        (percentId == 10 && (statusId == 3 || statusId == 2)) ||    // for Certitude = 100% -> Status = 'TERMINATED' || 'VALID'
                        (percentId >= 1 && percentId <= 9) && (statusId == 1));     // for  10% <= Certitude <= 90 -> Status = 'IN NEGOTIATION'
            }
        }
        return true;
    }
}
