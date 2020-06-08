package dania.app.web.mapper;

import dania.app.web.controllers.dto.MemberPositionViewDTO;
import dania.app.web.entities.MemberPositionViewEntity;
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
public class MemberPositionViewMapperImpl implements MemberPositionViewMapper {

    @Override
    public MemberPositionViewDTO memberPositionViewEntityToDTO(MemberPositionViewEntity memberPositionViewEntity) {
        if ( memberPositionViewEntity == null ) {
            return null;
        }

        MemberPositionViewDTO memberPositionViewDTO = new MemberPositionViewDTO();

        memberPositionViewDTO.setId( memberPositionViewEntity.getId() );
        memberPositionViewDTO.setLastName( memberPositionViewEntity.getLastName() );
        memberPositionViewDTO.setFirstName( memberPositionViewEntity.getFirstName() );
        memberPositionViewDTO.setIdProjectPositionFk( memberPositionViewEntity.getIdProjectPositionFk() );
        memberPositionViewDTO.setPercentPosition( memberPositionViewEntity.getPercentPosition() );
        memberPositionViewDTO.setPercentMember( memberPositionViewEntity.getPercentMember() );
        memberPositionViewDTO.setPercentProject( memberPositionViewEntity.getPercentProject() );
        memberPositionViewDTO.setStatusProject( memberPositionViewEntity.getStatusProject() );
        memberPositionViewDTO.setIdCalendarStartDateFk( memberPositionViewEntity.getIdCalendarStartDateFk() );
        memberPositionViewDTO.setIdCalendarEndDateFk( memberPositionViewEntity.getIdCalendarEndDateFk() );
        memberPositionViewDTO.setProjectName( memberPositionViewEntity.getProjectName() );

        return memberPositionViewDTO;
    }

    @Override
    public MemberPositionViewEntity memberPositionViewDTOToEntity(MemberPositionViewDTO memberPositionViewDTO) {
        if ( memberPositionViewDTO == null ) {
            return null;
        }

        MemberPositionViewEntity memberPositionViewEntity = new MemberPositionViewEntity();

        memberPositionViewEntity.setId( memberPositionViewDTO.getId() );
        memberPositionViewEntity.setLastName( memberPositionViewDTO.getLastName() );
        memberPositionViewEntity.setFirstName( memberPositionViewDTO.getFirstName() );
        memberPositionViewEntity.setIdProjectPositionFk( memberPositionViewDTO.getIdProjectPositionFk() );
        memberPositionViewEntity.setPercentPosition( memberPositionViewDTO.getPercentPosition() );
        memberPositionViewEntity.setPercentMember( memberPositionViewDTO.getPercentMember() );
        memberPositionViewEntity.setPercentProject( memberPositionViewDTO.getPercentProject() );
        memberPositionViewEntity.setStatusProject( memberPositionViewDTO.getStatusProject() );
        memberPositionViewEntity.setIdCalendarStartDateFk( memberPositionViewDTO.getIdCalendarStartDateFk() );
        memberPositionViewEntity.setIdCalendarEndDateFk( memberPositionViewDTO.getIdCalendarEndDateFk() );
        memberPositionViewEntity.setProjectName( memberPositionViewDTO.getProjectName() );

        return memberPositionViewEntity;
    }

    @Override
    public List<MemberPositionViewDTO> listMemberPositionViewEntityToDTO(List<MemberPositionViewEntity> memberPositionViewEntities) {
        if ( memberPositionViewEntities == null ) {
            return null;
        }

        List<MemberPositionViewDTO> list = new ArrayList<MemberPositionViewDTO>( memberPositionViewEntities.size() );
        for ( MemberPositionViewEntity memberPositionViewEntity : memberPositionViewEntities ) {
            list.add( memberPositionViewEntityToDTO( memberPositionViewEntity ) );
        }

        return list;
    }

    @Override
    public List<MemberPositionViewEntity> listMemberPositionViewDTOToEntity(List<MemberPositionViewDTO> memberPositionViewDTOS) {
        if ( memberPositionViewDTOS == null ) {
            return null;
        }

        List<MemberPositionViewEntity> list = new ArrayList<MemberPositionViewEntity>( memberPositionViewDTOS.size() );
        for ( MemberPositionViewDTO memberPositionViewDTO : memberPositionViewDTOS ) {
            list.add( memberPositionViewDTOToEntity( memberPositionViewDTO ) );
        }

        return list;
    }
}
