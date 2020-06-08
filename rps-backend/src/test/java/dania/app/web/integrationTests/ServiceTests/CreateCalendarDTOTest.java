package dania.app.web.integrationTests.ServiceTests;

import dania.app.web.controllers.dto.CalendarDTO;
import dania.app.web.integrationTests.config.TestConstants;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class CreateCalendarDTOTest {

    public CalendarDTO getCalendarDTO() {
        CalendarDTO calendarDTO = new CalendarDTO();
        calendarDTO.setId(TestConstants.ONE);
        calendarDTO.setBop(LocalDate.of(2018, 9, 23));
        calendarDTO.setEop(LocalDate.of(2019, 9, 29));
        calendarDTO.setQuarter(TestConstants.QUARTER_1);
        calendarDTO.setFiscalYear(2019);
        calendarDTO.setWeek(1);
        calendarDTO.setPeriod(1);
        return calendarDTO;
    }

    public CalendarDTO getCalendarWithIdTwo(){
        CalendarDTO  calendarDTO = new CalendarDTO();
        calendarDTO.setId("FY19-P01-W02");
        calendarDTO.setBop(LocalDate.of(2018,9,23));
        calendarDTO.setEop(LocalDate.of(2018, 9, 29));
        calendarDTO.setQuarter("Q1");
        calendarDTO.setFiscalYear(2020);
        calendarDTO.setPeriod(1);
        calendarDTO.setWeek(2);
        return calendarDTO;
    }
}
