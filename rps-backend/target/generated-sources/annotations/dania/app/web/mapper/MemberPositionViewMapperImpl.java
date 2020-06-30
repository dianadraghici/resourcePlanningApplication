package dania.app.web.mapper;

import dania.app.web.controllers.dto.MemberPositionViewDTO;
import dania.app.web.entities.MemberPositionViewEntity;
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
public class MemberPositionViewMapperImpl implements MemberPositionViewMapper {

    @Override
    public MemberPositionViewDTO memberPositionViewEntityToDTO(MemberPositionViewEntity memberPositionViewEntity) {
        if ( memberPositionViewEntity == null ) {
            return null;
        }

        MemberPositionViewDTO memberPositionViewDTO = new MemberPositionViewDTO();

        return memberPositionViewDTO;
    }

    @Override
    public MemberPositionViewEntity memberPositionViewDTOToEntity(MemberPositionViewDTO memberPositionViewDTO) {
        if ( memberPositionViewDTO == null ) {
            return null;
        }

        MemberPositionViewEntity memberPositionViewEntity = new MemberPositionViewEntity();

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
