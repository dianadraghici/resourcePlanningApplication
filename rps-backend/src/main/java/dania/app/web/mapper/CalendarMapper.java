package dania.app.web.mapper;

import dania.app.web.controllers.dto.CalendarDTO;
import dania.app.web.entities.CalendarEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CalendarMapper {

    CalendarDTO calendarEntityToDTO(CalendarEntity calendarEntity);

    CalendarEntity calendarDTOToEntity(CalendarDTO calendarDTO);

    List<CalendarDTO> listCalendarEntityToDTO(List<CalendarEntity> calendarEntities);

    List<CalendarEntity> listCalendarDTOToEntity(List<CalendarDTO> calendarDTOS);

}
