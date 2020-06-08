package dania.app.web.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "projectCode", "projectName", "statusId", "percentId"})
@Entity
@Table(name = "projects", schema = "planification", catalog = "")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String projectCode;
    private String projectName;
    private Integer statusId;
    private Integer percentId;

    @ManyToOne
    @JoinColumn(name = "id_calendar_start_date_fk", referencedColumnName = "id", nullable = false)
    private CalendarEntity startDateCalendarEntity;

    @ManyToOne
    @JoinColumn(name = "id_calendar_end_date_fk", referencedColumnName = "id")
    private CalendarEntity endDateCalendarEntity;
}
