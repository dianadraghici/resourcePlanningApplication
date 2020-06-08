package dania.app.web.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "positionId", "numberPositions", "percentId"})
@Entity
@Table(name = "project_position", schema = "planification", catalog = "")
public class ProjectPositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer positionId;
    private Integer numberPositions;
    private Integer percentId;

    @ManyToOne
    @JoinColumn(name = "id_projects_fk", referencedColumnName = "id", nullable = false)
    private ProjectEntity projectEntity;

    @ManyToOne
    @JoinColumn(name = "id_calendar_start_date_fk", referencedColumnName = "id", nullable = false)
    private CalendarEntity startDateCalendarEntity;

    @ManyToOne
    @JoinColumn(name = "id_calendar_end_date_fk", referencedColumnName = "id")
    private CalendarEntity endDateCalendarEntity;

}
