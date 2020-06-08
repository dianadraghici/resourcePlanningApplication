package dania.app.web.mapper;

import dania.app.web.controllers.dto.MemberPositionViewDTO;
import dania.app.web.entities.MemberPositionViewEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberPositionViewMapper {

    MemberPositionViewDTO memberPositionViewEntityToDTO(MemberPositionViewEntity memberPositionViewEntity);

    MemberPositionViewEntity memberPositionViewDTOToEntity(MemberPositionViewDTO memberPositionViewDTO);

    List<MemberPositionViewDTO> listMemberPositionViewEntityToDTO(
            List<MemberPositionViewEntity> memberPositionViewEntities);

    List<MemberPositionViewEntity> listMemberPositionViewDTOToEntity(
            List<MemberPositionViewDTO> memberPositionViewDTOS);
}
