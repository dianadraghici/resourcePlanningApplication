package dania.app.web.mapper;

import dania.app.web.controllers.dto.MemberDTO;
import dania.app.web.entities.MemberEntity;
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
public class MemberMapperImpl extends MemberMapper {

    @Override
    public MemberEntity memberDTOToEntity(MemberDTO memberDTO) {
        if ( memberDTO == null ) {
            return null;
        }

        MemberEntity memberEntity = new MemberEntity();

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
