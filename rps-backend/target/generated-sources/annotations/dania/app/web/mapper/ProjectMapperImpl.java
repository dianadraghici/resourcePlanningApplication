package dania.app.web.mapper;

import dania.app.web.controllers.dto.CalendarDTO;
import dania.app.web.controllers.dto.ProjectDTO;
import dania.app.web.entities.CalendarEntity;
import dania.app.web.entities.ProjectEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-26T14:26:03+0300",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 13.0.2 (N/A)"
)
@Component
public class ProjectMapperImpl implements ProjectMapper {

    @Override
    public ProjectDTO projectEntityToDTO(ProjectEntity projectEntity) {
        if ( projectEntity == null ) {
            return null;
        }

        ProjectDTO projectDTO = new ProjectDTO();

        projectDTO.setStartDateCalendarDTO( calendarEntityToCalendarDTO( projectEntity.getStartDateCalendarEntity() ) );
        projectDTO.setEndDateCalendarDTO( calendarEntityToCalendarDTO( projectEntity.getEndDateCalendarEntity() ) );
        projectDTO.setId( projectEntity.getId() );
        projectDTO.setProjectCode( projectEntity.getProjectCode() );
        projectDTO.setProjectName( projectEntity.getProjectName() );
        projectDTO.setStatusId( projectEntity.getStatusId() );
        projectDTO.setPercentId( projectEntity.getPercentId() );

        return projectDTO;
    }

    @Override
    public ProjectEntity projectDTOToEntity(ProjectDTO projectDTO) {
        if ( projectDTO == null ) {
            return null;
        }

        ProjectEntity projectEntity = new ProjectEntity();

        projectEntity.setEndDateCalendarEntity( calendarDTOToCalendarEntity( projectDTO.getEndDateCalendarDTO() ) );
        projectEntity.setStartDateCalendarEntity( calendarDTOToCalendarEntity( projectDTO.getStartDateCalendarDTO() ) );
        projectEntity.setId( projectDTO.getId() );
        projectEntity.setProjectCode( projectDTO.getProjectCode() );
        projectEntity.setProjectName( projectDTO.getProjectName() );
        projectEntity.setStatusId( projectDTO.getStatusId() );
        projectEntity.setPercentId( projectDTO.getPercentId() );

        return projectEntity;
    }

    @Override
    public List<ProjectDTO> listProjectEntityToDTO(List<ProjectEntity> projectEntities) {
        if ( projectEntities == null ) {
            return null;
        }

        List<ProjectDTO> list = new ArrayList<ProjectDTO>( projectEntities.size() );
        for ( ProjectEntity projectEntity : projectEntities ) {
            list.add( projectEntityToDTO( projectEntity ) );
        }

        return list;
    }

    @Override
    public List<ProjectEntity> listProjectDTOToEntity(List<ProjectDTO> projectDTOs) {
        if ( projectDTOs == null ) {
            return null;
        }

        List<ProjectEntity> list = new ArrayList<ProjectEntity>( projectDTOs.size() );
        for ( ProjectDTO projectDTO : projectDTOs ) {
            list.add( projectDTOToEntity( projectDTO ) );
        }

        return list;
    }

    protected CalendarDTO calendarEntityToCalendarDTO(CalendarEntity calendarEntity) {
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

    protected CalendarEntity calendarDTOToCalendarEntity(CalendarDTO calendarDTO) {
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
}
