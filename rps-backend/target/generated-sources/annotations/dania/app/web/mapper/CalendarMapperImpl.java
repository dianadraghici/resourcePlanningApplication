package dania.app.web.mapper;

import dania.app.web.controllers.dto.CalendarDTO;
import dania.app.web.entities.CalendarEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-08T09:32:12+0300",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 13.0.2 (Oracle Corporation)"
)
@Component
public class CalendarMapperImpl implements CalendarMapper {

    @Override
    public CalendarDTO calendarEntityToDTO(CalendarEntity calendarEntity) {
        if ( calendarEntity == null ) {
            return null;
        }

        CalendarDTO calendarDTO = new CalendarDTO();

        calendarDTO.setId( calendarEntity.getId() );
        calendarDTO.setFiscalYear( calendarEntity.getFiscalYear() );
        calendarDTO.setPeriod( calendarEntity.getPeriod() );
        calendarDTO.setWeek( calendarEntity.getWeek() );
        calendarDTO.setBop( calendarEntity.getBop() );
        calendarDTO.setEop( calendarEntity.getEop() );
        calendarDTO.setQuarter( calendarEntity.getQuarter() );

        return calendarDTO;
    }

    @Override
    public CalendarEntity calendarDTOToEntity(CalendarDTO calendarDTO) {
        if ( calendarDTO == null ) {
            return null;
        }

        CalendarEntity calendarEntity = new CalendarEntity();

        calendarEntity.setId( calendarDTO.getId() );
        calendarEntity.setFiscalYear( calendarDTO.getFiscalYear() );
        calendarEntity.setPeriod( calendarDTO.getPeriod() );
        calendarEntity.setWeek( calendarDTO.getWeek() );
        calendarEntity.setBop( calendarDTO.getBop() );
        calendarEntity.setEop( calendarDTO.getEop() );
        calendarEntity.setQuarter( calendarDTO.getQuarter() );

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
