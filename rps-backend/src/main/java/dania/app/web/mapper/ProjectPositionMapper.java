package dania.app.web.mapper;

import dania.app.web.controllers.dto.ProjectPositionDTO;
import dania.app.web.entities.ProjectPositionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectPositionMapper {


    @Mappings({
            @Mapping(target = "projectDTO", source = "projectPositionEntity.projectEntity"),
            @Mapping(target = "projectDTO.startDateCalendarDTO", source = "projectEntity.startDateCalendarEntity"),
            @Mapping(target = "projectDTO.endDateCalendarDTO", source = "projectEntity.endDateCalendarEntity"),
            @Mapping(target = "startDateCalendarDTO", source = "projectPositionEntity.startDateCalendarEntity"),
            @Mapping(target = "endDateCalendarDTO", source = "projectPositionEntity.endDateCalendarEntity")
    })
    ProjectPositionDTO projectPositionEntityToDTO(ProjectPositionEntity projectPositionEntity);

    @Mappings({
            @Mapping(target = "projectEntity", source = "projectPositionDTO.projectDTO"),
            @Mapping(target = "projectEntity.startDateCalendarEntity", source = "projectPositionDTO.projectDTO.startDateCalendarDTO"),
            @Mapping(target = "projectEntity.endDateCalendarEntity", source = "projectPositionDTO.projectDTO.endDateCalendarDTO"),
            @Mapping(target = "startDateCalendarEntity", source = "projectPositionDTO.startDateCalendarDTO"),
            @Mapping(target = "endDateCalendarEntity", source = "projectPositionDTO.endDateCalendarDTO")
    })
    ProjectPositionEntity projectPositionDTOToEntity(ProjectPositionDTO projectPositionDTO);

    List<ProjectPositionDTO> listProjectPositionEntityToDTO(List<ProjectPositionEntity> projectPositionEntities);

    List<ProjectPositionEntity> listProjectPositionDTOToEntity(List<ProjectPositionDTO> projectPositionDTOs);

}
