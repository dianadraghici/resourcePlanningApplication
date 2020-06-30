package dania.app.web.mapper;

import dania.app.web.controllers.dto.CalendarDTO;
import dania.app.web.entities.CalendarEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-26T14:39:54+0300",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 3.22.0.v20200530-2032, environment: Java 1.8.0_221 (Oracle Corporation)"
)
@Component
public class CalendarMapperImpl implements CalendarMapper {

    @Override
    public CalendarDTO calendarEntityToDTO(CalendarEntity calendarEntity) {
        if ( calendarEntity == null ) {
            return null;
        }

        CalendarDTO calendarDTO = new CalendarDTO();

        return calendarDTO;
    }

    @Override
    public CalendarEntity calendarDTOToEntity(CalendarDTO calendarDTO) {
        if ( calendarDTO == null ) {
            return null;
        }

        CalendarEntity calendarEntity = new CalendarEntity();

        return calendarEntity;
    }

    @Override
    public List<CalendarDTO> listCalendarEntityToDTO(List<CalendarEntity> calendarEntities) {
        if ( calendarEntities == null ) {
            return null;
        }

        List<CalendarDTO> list = new ArrayList<CalendarDTO>( calendarEntities.size() );
        for ( CalendarEntity calendarEntity : calendarEntities ) {
            list.add( calendarEntityToDTO( calendarEntity ) );
        }

        return list;
    }

    @Override
    public List<CalendarEntity> listCalendarDTOToEntity(List<CalendarDTO> calendarDTOS) {
        if ( calendarDTOS == null ) {
            return null;
        }

        List<CalendarEntity> list = new ArrayList<CalendarEntity>( calendarDTOS.size() );
        for ( CalendarDTO calendarDTO : calendarDTOS ) {
            list.add( calendarDTOToEntity( calendarDTO ) );
        }

        return list;
    }
}
