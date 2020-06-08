package dania.app.web.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "lastName", "firstName", "idProjectPositionFk", "percentPosition", "percentMember",
        "percentProject", "statusProject", "idCalendarStartDateFk", "idCalendarEndDateFk", "projectName"})
@Entity
@Table(name = "member_position_view", schema = "planification", catalog = "")
public class MemberPositionViewEntity {

    @Id
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

    public String concatFirstLastName(){
        return this.firstName + ' ' + this.lastName;
    }
}
