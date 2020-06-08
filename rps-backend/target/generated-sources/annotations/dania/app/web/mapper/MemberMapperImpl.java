package dania.app.web.mapper;

import dania.app.web.controllers.dto.MemberDTO;
import dania.app.web.entities.MemberEntity;
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
public class MemberMapperImpl extends MemberMapper {

    @Override
    public MemberEntity memberDTOToEntity(MemberDTO memberDTO) {
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

    @Override
    public List<MemberDTO> listMemberEntityToDTO(List<MemberEntity> memberEntities) {
        if ( memberEntities == null ) {
            return null;
        }

        List<MemberDTO> list = new ArrayList<MemberDTO>( memberEntities.size() );
        for ( MemberEntity memberEntity : memberEntities ) {
            list.add( memberEntityToDTO( memberEntity ) );
        }

        return list;
    }

    @Override
    public List<MemberEntity> listMemberDTOToEntity(List<MemberDTO> memberDTOS) {
        if ( memberDTOS == null ) {
            return null;
        }

        List<MemberEntity> list = new ArrayList<MemberEntity>( memberDTOS.size() );
        for ( MemberDTO memberDTO : memberDTOS ) {
            list.add( memberDTOToEntity( memberDTO ) );
        }

        return list;
    }
}
