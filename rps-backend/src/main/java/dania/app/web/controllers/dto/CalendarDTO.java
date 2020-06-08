package dania.app.web.controllers.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDTO implements Comparable {

    private String id;
    private Integer fiscalYear;
    private Integer period;
    private Integer week;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDate bop;

    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDate eop;
    private String quarter;

    public String mapKeyWeek() {
        return String.valueOf(this.getFiscalYear()) + '-' + String.valueOf(this.getWeek());
    }

    public int compareToWeek(Object o) {
        return this.mapKeyWeek().compareTo(((CalendarDTO) o).mapKeyWeek());
    }

    public String mapKey() {
        return String.valueOf(this.getFiscalYear()) + '/' + this.getQuarter();
    }

    @Override
    public int compareTo(Object o) {
        return this.mapKey().compareTo(((CalendarDTO) o).mapKey());
    }
}
