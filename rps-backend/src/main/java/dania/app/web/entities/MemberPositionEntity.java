package dania.app.web.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "percentId"})
@Entity
@Table(name = "member_position", schema = "planification", catalog = "")
@DynamicUpdate
public class MemberPositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer percentId;

    @ManyToOne
    @JoinColumn(name = "id_project_position_fk", referencedColumnName = "", nullable = false)
    private ProjectPositionEntity projectPositionEntity;

    @ManyToOne
    @JoinColumn(name = "id_members_fk", referencedColumnName = "id", nullable = false)
    private MemberEntity memberEntity;

    @ManyToOne
    @JoinColumn(name = "id_calendar_start_date_fk", referencedColumnName = "id", nullable = false)
    private CalendarEntity startDateCalendarEntity;

    @ManyToOne
    @JoinColumn(name = "id_calendar_end_date_fk", referencedColumnName = "id", nullable = false)
    private CalendarEntity endDateCalendarEntity;
}
