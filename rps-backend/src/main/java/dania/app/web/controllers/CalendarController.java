package dania.app.web.controllers;

import dania.app.web.controllers.dto.CalendarDTO;
import dania.app.web.service.CalendarService;
import dania.app.web.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static dania.app.web.utils.Cleaner.getSortedMap;
import static java.util.stream.Collectors.groupingBy;

@RestController
@RequestMapping(Constants.CALENDAR_CONTROLLER)
public class CalendarController {

    private CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping
    List<CalendarDTO> getCalendarList() {
        return calendarService.findAll();
    }

    @GetMapping
    @RequestMapping(path = Constants.GET_MAP_CALENDAR_BY_QUARTER_YEAR)
    public Map<String, List<CalendarDTO>> getMapCalendarByQuarterYear() {
        return getSortedMap(calendarService.findAll().stream()
                .collect(groupingBy(CalendarDTO::mapKey)));
    }
}

