package dania.app.web.mapper;

import dania.app.web.controllers.dto.MemberDTO;
import dania.app.web.entities.MemberEntity;
import dania.app.web.service.ParameterService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract  class MemberMapper {

//    MemberDTO memberEntityToDTO(MemberEntity memberEntity);
    @Autowired
    ParameterService parameterService;

    public abstract MemberEntity memberDTOToEntity(MemberDTO memberDTO);

    public abstract List<MemberDTO> listMemberEntityToDTO(List<MemberEntity> memberEntities);

    public abstract List<MemberEntity> listMemberDTOToEntity(List<MemberDTO> memberDTOS);


    public MemberDTO memberEntityToDTO(MemberEntity memberEntity) {
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
        memberDTO.setTechnologyDescription( parameterService.findByIdTechnology(memberEntity.getTechnologyId()).getDescription());
        memberDTO.setComment( memberEntity.getComment() );

        return  memberDTO;

    }
}
