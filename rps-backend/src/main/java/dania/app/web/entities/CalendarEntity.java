package dania.app.web.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "fiscalYear", "period", "week", "bop", "eop", "quarter"})
@Entity
@Table(name = "calendar", schema = "planification", catalog = "")
public class CalendarEntity {

    @Id
    private String id;

    private Integer fiscalYear;
    private Integer period;
    private Integer week;
    private LocalDate bop;
    private LocalDate eop;
    private String quarter;
}
