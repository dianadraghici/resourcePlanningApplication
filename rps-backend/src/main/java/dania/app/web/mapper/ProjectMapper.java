package dania.app.web.mapper;

import dania.app.web.controllers.dto.ProjectDTO;
import dania.app.web.entities.ProjectEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mappings({
            @Mapping(target = "startDateCalendarDTO", source = "projectEntity.startDateCalendarEntity"),
            @Mapping(target = "endDateCalendarDTO", source = "projectEntity.endDateCalendarEntity")
    })
    ProjectDTO projectEntityToDTO(ProjectEntity projectEntity);

    @Mappings({
            @Mapping(target = "startDateCalendarEntity", source = "projectDTO.startDateCalendarDTO"),
            @Mapping(target = "endDateCalendarEntity", source = "projectDTO.endDateCalendarDTO")
    })
    ProjectEntity projectDTOToEntity(ProjectDTO projectDTO);

    List<ProjectDTO> listProjectEntityToDTO(List<ProjectEntity> projectEntities);

    List<ProjectEntity> listProjectDTOToEntity(List<ProjectDTO> projectDTOs);

}
