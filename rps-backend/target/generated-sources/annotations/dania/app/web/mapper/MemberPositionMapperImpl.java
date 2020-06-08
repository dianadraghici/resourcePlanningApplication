package dania.app.web.mapper;

import dania.app.web.controllers.dto.CalendarDTO;
import dania.app.web.controllers.dto.MemberDTO;
import dania.app.web.controllers.dto.MemberPositionDTO;
import dania.app.web.controllers.dto.ProjectDTO;
import dania.app.web.controllers.dto.ProjectPositionDTO;
import dania.app.web.entities.CalendarEntity;
import dania.app.web.entities.MemberEntity;
import dania.app.web.entities.MemberPositionEntity;
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
public class MemberPositionMapperImpl implements MemberPositionMapper {

    @Override
    public MemberPositionDTO memberPositionEntityToDTO(MemberPositionEntity memberPositionEntity) {
        if ( memberPositionEntity == null ) {
            return null;
        }

        MemberPositionDTO memberPositionDTO = new MemberPositionDTO();

        memberPositionDTO.setProjectPositionDTO( projectPositionEntityToProjectPositionDTO( memberPositionEntity.getProjectPositionEntity() ) );
        memberPositionDTO.setMemberDTO( memberEntityToMemberDTO( memberPositionEntity.getMemberEntity() ) );
        memberPositionDTO.setStartDateCalendarDTO( calendarEntityToCalendarDTO( memberPositionEntity.getStartDateCalendarEntity() ) );
        memberPositionDTO.setEndDateCalendarDTO( calendarEntityToCalendarDTO( memberPositionEntity.getEndDateCalendarEntity() ) );
        memberPositionDTO.setId( memberPositionEntity.getId() );
        memberPositionDTO.setPercentId( memberPositionEntity.getPercentId() );

        return memberPositionDTO;
    }

    @Override
    public MemberPositionEntity memberPositionDTOToEntity(MemberPositionDTO memberPositionDTO) {
        if ( memberPositionDTO == null ) {
            return null;
        }

        MemberPositionEntity memberPositionEntity = new MemberPositionEntity();

        memberPositionEntity.setProjectPositionEntity( projectPositionDTOToProjectPositionEntity( memberPositionDTO.getProjectPositionDTO() ) );
        memberPositionEntity.setEndDateCalendarEntity( calendarDTOToCalendarEntity( memberPositionDTO.getEndDateCalendarDTO() ) );
        memberPositionEntity.setStartDateCalendarEntity( calendarDTOToCalendarEntity( memberPositionDTO.getStartDateCalendarDTO() ) );
        memberPositionEntity.setMemberEntity( memberDTOToMemberEntity( memberPositionDTO.getMemberDTO() ) );
        memberPositionEntity.setId( memberPositionDTO.getId() );
        memberPositionEntity.setPercentId( memberPositionDTO.getPercentId() );

        return memberPositionEntity;
    }

    @Override
    public List<MemberPositionDTO> listMemberPositionEntityToDTO(List<MemberPositionEntity> memberPositionEntities) {
        if ( memberPositionEntities == null ) {
            return null;
        }

        List<MemberPositionDTO> list = new ArrayList<MemberPositionDTO>( memberPositionEntities.size() );
        for ( MemberPositionEntity memberPositionEntity : memberPositionEntities ) {
            list.add( memberPositionEntityToDTO( memberPositionEntity ) );
        }

        return list;
    }

    @Override
    public List<MemberPositionEntity> listMemberPositionDTOToEntity(List<MemberPositionDTO> memberPositionDTOS) {
        if ( memberPositionDTOS == null ) {
            return null;
        }

        List<MemberPositionEntity> list = new ArrayList<MemberPositionEntity>( memberPositionDTOS.size() );
        for ( MemberPositionDTO memberPositionDTO : memberPositionDTOS ) {
            list.add( memberPositionDTOToEntity( memberPositionDTO ) );
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

    protected ProjectPositionDTO projectPositionEntityToProjectPositionDTO(ProjectPositionEntity projectPositionEntity) {
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

    protected MemberDTO memberEntityToMemberDTO(MemberEntity memberEntity) {
        if ( memberEntity == null ) {
            return null;
        }

        MemberDTO memberDTO = new MemberDTO();

        memberDTO.setId( memberEntity.getId() );
        memberDTO.setStaffNumber( memberEntity.getStaffNumber() );
        memberDTO.setLastName( memberEntity.getLastName() );
        memberDTO.setFirstName( memberEntity.getFirstName() );
        memberDTO.setFlag( memberEntity.getFlag() );
        memberDTO.setTechnologyId( memberEntity.getTechnologyId() );
        memberDTO.setComment( memberEntity.getComment() );

        return memberDTO;
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

    protected ProjectPositionEntity projectPositionDTOToProjectPositionEntity(ProjectPositionDTO projectPositionDTO) {
        if ( projectPositionDTO == null ) {
            return null;
        }

        ProjectPositionEntity projectPositionEntity = new ProjectPositionEntity();

        projectPositionEntity.setProjectEntity( projectDTOToProjectEntity( projectPositionDTO.getProjectDTO() ) );
        projectPositionEntity.setStartDateCalendarEntity( calendarDTOToCalendarEntity( projectPositionDTO.getStartDateCalendarDTO() ) );
        projectPositionEntity.setEndDateCalendarEntity( calendarDTOToCalendarEntity( projectPositionDTO.getEndDateCalendarDTO() ) );
        projectPositionEntity.setId( projectPositionDTO.getId() );
        projectPositionEntity.setPositionId( projectPositionDTO.getPositionId() );
        projectPositionEntity.setNumberPositions( projectPositionDTO.getNumberPositions() );
        projectPositionEntity.setPercentId( projectPositionDTO.getPercentId() );

        return projectPositionEntity;
    }

    protected MemberEntity memberDTOToMemberEntity(MemberDTO memberDTO) {
        if ( memberDTO == null ) {
            return null;
        }

        MemberEntity memberEntity = new MemberEntity();

        memberEntity.setId( memberDTO.getId() );
        memberEntity.setStaffNumber( memberDTO.getStaffNumber() );
        memberEntity.setLastName( memberDTO.getLastName() );
        memberEntity.setFirstName( memberDTO.getFirstName() );
        memberEntity.setFlag( memberDTO.getFlag() );
        memberEntity.setTechnologyId( memberDTO.getTechnologyId() );
        memberEntity.setComment( memberDTO.getComment() );

        return memberEntity;
    }
}
