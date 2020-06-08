package dania.app.web.mapper;

import dania.app.web.controllers.dto.MemberPositionDTO;
import dania.app.web.entities.MemberPositionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberPositionMapper {
    @Mappings({
            @Mapping(target = "projectPositionDTO.projectDTO", source = "projectPositionEntity.projectEntity"),
            @Mapping(target = "projectPositionDTO.startDateCalendarDTO", source = "projectPositionEntity.startDateCalendarEntity"),
            @Mapping(target = "projectPositionDTO.endDateCalendarDTO", source = "projectPositionEntity.endDateCalendarEntity"),
            @Mapping(target = "projectPositionDTO.projectDTO.startDateCalendarDTO", source = "projectPositionEntity.projectEntity.startDateCalendarEntity"),
            @Mapping(target = "projectPositionDTO.projectDTO.endDateCalendarDTO", source = "projectPositionEntity.projectEntity.endDateCalendarEntity"),
            @Mapping(target = "projectPositionDTO", source = "memberPositionEntity.projectPositionEntity"),
            @Mapping(target = "memberDTO", source = "memberPositionEntity.memberEntity"),
            @Mapping(target = "startDateCalendarDTO", source = "memberPositionEntity.startDateCalendarEntity"),
            @Mapping(target = "endDateCalendarDTO", source = "memberPositionEntity.endDateCalendarEntity")
    })
    MemberPositionDTO memberPositionEntityToDTO(MemberPositionEntity memberPositionEntity);

    @Mappings({
            @Mapping(target = "projectPositionEntity.projectEntity", source = "projectPositionDTO.projectDTO"),
            @Mapping(target = "projectPositionEntity.startDateCalendarEntity", source = "projectPositionDTO.startDateCalendarDTO"),
            @Mapping(target = "projectPositionEntity.endDateCalendarEntity", source = "projectPositionDTO.endDateCalendarDTO"),
            @Mapping(target = "projectPositionEntity.projectEntity.startDateCalendarEntity", source = "projectPositionDTO.projectDTO.startDateCalendarDTO"),
            @Mapping(target = "projectPositionEntity.projectEntity.endDateCalendarEntity", source = "projectPositionDTO.projectDTO.endDateCalendarDTO"),
            @Mapping(target = "projectPositionEntity", source = "memberPositionDTO.projectPositionDTO"),
            @Mapping(target = "memberEntity", source = "memberPositionDTO.memberDTO"),
            @Mapping(target = "startDateCalendarEntity", source = "memberPositionDTO.startDateCalendarDTO"),
            @Mapping(target = "endDateCalendarEntity", source = "memberPositionDTO.endDateCalendarDTO")
    })
    MemberPositionEntity memberPositionDTOToEntity(MemberPositionDTO memberPositionDTO);

    List<MemberPositionDTO> listMemberPositionEntityToDTO(List<MemberPositionEntity> memberPositionEntities);

    List<MemberPositionEntity> listMemberPositionDTOToEntity(List<MemberPositionDTO> memberPositionDTOS);

}
