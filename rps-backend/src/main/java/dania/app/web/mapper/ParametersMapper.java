package dania.app.web.mapper;

import dania.app.web.controllers.dto.ParametersDTO;
import dania.app.web.entities.ParametersEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParametersMapper {

    ParametersDTO parameterEntityToDTO(ParametersEntity parameterEntity);

    ParametersEntity parameterDTOToEntity(ParametersDTO parameterDTO);

    List<ParametersDTO> listParamatersEntityToDTO(List<ParametersEntity> parametersEntities);

    List<ParametersEntity> listParamatersDTOToEntity(List<ParametersDTO> calendarDTOs);

}
