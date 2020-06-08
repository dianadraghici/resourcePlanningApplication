package dania.app.web.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberPositionViewDTO {

    private String id;

    private String lastName;
    private String firstName;
    private Integer idProjectPositionFk;
    private String percentPosition;
    private String percentMember;
    private String percentProject;
    private String statusProject;
    private String idCalendarStartDateFk;
    private String idCalendarEndDateFk;
    private String projectName;

    public String concatFirstLastName() {
        return this.firstName + ' ' + this.lastName;
    }
}
