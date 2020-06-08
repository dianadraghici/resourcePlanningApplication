package dania.app.web.mapper;

import dania.app.web.controllers.dto.CalendarDTO;
import dania.app.web.controllers.dto.ProjectDTO;
import dania.app.web.controllers.dto.ProjectPositionDTO;
import dania.app.web.entities.CalendarEntity;
import dania.app.web.entities.ProjectEntity;
import dania.app.web.entities.ProjectPositionEntity;
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
public class ProjectPositionMapperImpl implements ProjectPositionMapper {

    @Override
    public ProjectPositionDTO projectPositionEntityToDTO(ProjectPositionEntity projectPositionEntity) {
        if ( projectPositionEntity == null ) {
            return null;
        }

        ProjectPositionDTO projectPositionDTO = new ProjectPositionDTO();

        projectPositionDTO.setProjectDTO( projectEntityToProjectDTO( projectPositionEntity.getProjectEntity() ) );
        projectPositionDTO.setStartDateCalendarDTO( calendarEntityToCalendarDTO( projectPositionEntity.getStartDateCalendarEntity() ) );
        projectPositionDTO.setEndDateCalendarDTO( calendarEntityToCalendarDTO( projectPositionEntity.getEndDateCalendarEntity() ) );
        projectPositionDTO.setId( projectPositionEntity.getId() );
        projectPositionDTO.setPositionId( projectPositionEntity.getPositionId() );
        projectPositionDTO.setNumberPositions( projectPositionEntity.getNumberPositions() );
        projectPositionDTO.setPercentId( projectPositionEntity.getPercentId() );

        return projectPositionDTO;
    }

    @Override
    public ProjectPositionEntity projectPositionDTOToEntity(ProjectPositionDTO projectPositionDTO) {
        if ( projectPositionDTO == null ) {
            return null;
        }

        ProjectPositionEntity projectPositionEntity = new ProjectPositionEntity();

        projectPositionEntity.setProjectEntity( projectDTOToProjectEntity( projectPositionDTO.getProjectDTO() ) );
        projectPositionEntity.setEndDateCalendarEntity( calendarDTOToCalendarEntity( projectPositionDTO.getEndDateCalendarDTO() ) );
        projectPositionEntity.setStartDateCalendarEntity( calendarDTOToCalendarEntity( projectPositionDTO.getStartDateCalendarDTO() ) );
        projectPositionEntity.setId( projectPositionDTO.getId() );
        projectPositionEntity.setPositionId( projectPositionDTO.getPositionId() );
        projectPositionEntity.setNumberPositions( projectPositionDTO.getNumberPositions() );
        projectPositionEntity.setPercentId( projectPositionDTO.getPercentId() );

        return projectPositionEntity;
    }

    @Override
    public List<ProjectPositionDTO> listProjectPositionEntityToDTO(List<ProjectPositionEntity> projectPositionEntities) {
        if ( projectPositionEntities == null ) {
            return null;
        }

        List<ProjectPositionDTO> list = new ArrayList<ProjectPositionDTO>( projectPositionEntities.size() );
        for ( ProjectPositionEntity projectPositionEntity : projectPositionEntities ) {
            list.add( projectPositionEntityToDTO( projectPositionEntity ) );
        }

        return list;
    }

    @Override
    public List<ProjectPositionEntity> listProjectPositionDTOToEntity(List<ProjectPositionDTO> projectPositionDTOs) {
        if ( projectPositionDTOs == null ) {
            return null;
        }

        List<ProjectPositionEntity> list = new ArrayList<ProjectPositionEntity>( projectPositionDTOs.size() );
        for ( ProjectPositionDTO projectPositionDTO : projectPositionDTOs ) {
            list.add( projectPositionDTOToEntity( projectPositionDTO ) );
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

    protected ProjectDTO projectEntityToProjectDTO(ProjectEntity projectEntity) {
        if ( projectEntity == null ) {
            return null;
        }

        ProjectDTO projectDTO = new ProjectDTO();

        projectDTO.setEndDateCalendarDTO( calendarEntityToCalendarDTO( projectEntity.getEndDateCalendarEntity() ) );
        projectDTO.setStartDateCalendarDTO( calendarEntityToCalendarDTO( projectEntity.getStartDateCalendarEntity() ) );
        projectDTO.setId( projectEntity.getId() );
        projectDTO.setProjectCode( projectEntity.getProjectCode() );
        projectDTO.setProjectName( projectEntity.getProjectName() );
        projectDTO.setStatusId( projectEntity.getStatusId() );
        projectDTO.setPercentId( projectEntity.getPercentId() );

        return projectDTO;
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

    protected ProjectEntity projectDTOToProjectEntity(ProjectDTO projectDTO) {
        if ( projectDTO == null ) {
            return null;
        }

        ProjectEntity projectEntity = new ProjectEntity();

        projectEntity.setStartDateCalendarEntity( calendarDTOToCalendarEntity( projectDTO.getStartDateCalendarDTO() ) );
        projectEntity.setEndDateCalendarEntity( calendarDTOToCalendarEntity( projectDTO.getEndDateCalendarDTO() ) );
        projectEntity.setId( projectDTO.getId() );
        projectEntity.setProjectCode( projectDTO.getProjectCode() );
        projectEntity.setProjectName( projectDTO.getProjectName() );
        projectEntity.setStatusId( projectDTO.getStatusId() );
        projectEntity.setPercentId( projectDTO.getPercentId() );

        return projectEntity;
    }
}
