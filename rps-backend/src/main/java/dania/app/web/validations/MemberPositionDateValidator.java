package dania.app.web.validations;

import dania.app.web.controllers.dto.MemberPositionDTO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class MemberPositionDateValidator implements ConstraintValidator<MemberPositionDateConstraints, MemberPositionDTO> {

    @Override
    public void initialize(MemberPositionDateConstraints memberPositionDateConstraints) {
    }

    /*
     * returns true if:
     *  -   startDate for MemberPosition is before endDate for MemberPosition
     *  -   startDate for MemberPosition is after startDate for ProjectPosition
     *  -   endDate for MemberPosition is after endDate for ProjectPosition
     * */
    public boolean isValid(MemberPositionDTO value, ConstraintValidatorContext context) {

        if(value.getStartDateCalendarDTO() !=null  ){
            LocalDate startDateMemberPosition = value.getStartDateCalendarDTO().getBop();
            if (value.getEndDateCalendarDTO() != null) {
                LocalDate endDateMemberPosition = value.getEndDateCalendarDTO().getEop();
                if (value.getProjectPositionDTO() != null) {
                    if (value.getProjectPositionDTO().getStartDateCalendarDTO() != null) {
                        LocalDate startDateProjectPosition = value.getProjectPositionDTO().getStartDateCalendarDTO().getBop();
                        if (value.getProjectPositionDTO().getEndDateCalendarDTO() != null) {
                            LocalDate endDateProjectPosition = value.getProjectPositionDTO().getEndDateCalendarDTO().getEop();
                            return (startDateMemberPosition.compareTo(endDateMemberPosition) <= 0 &&
                                    startDateMemberPosition.compareTo(startDateProjectPosition) >= 0 &&
                                    endDateMemberPosition.compareTo(endDateProjectPosition) >= 0);
                        }
                    }
                }
            }
        }


        return true;
    }
}
