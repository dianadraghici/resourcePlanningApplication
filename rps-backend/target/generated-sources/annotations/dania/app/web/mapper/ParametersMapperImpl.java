package dania.app.web.mapper;

import dania.app.web.controllers.dto.ParametersDTO;
import dania.app.web.entities.ParametersEntity;
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
public class ParametersMapperImpl implements ParametersMapper {

    @Override
    public ParametersDTO parameterEntityToDTO(ParametersEntity parameterEntity) {
        if ( parameterEntity == null ) {
            return null;
        }

        ParametersDTO parametersDTO = new ParametersDTO();

        parametersDTO.setType( parameterEntity.getType() );
        parametersDTO.setId( parameterEntity.getId() );
        parametersDTO.setDescription( parameterEntity.getDescription() );

        return parametersDTO;
    }

    @Override
    public ParametersEntity parameterDTOToEntity(ParametersDTO parameterDTO) {
        if ( parameterDTO == null ) {
            return null;
        }

        ParametersEntity parametersEntity = new ParametersEntity();

        parametersEntity.setType( parameterDTO.getType() );
        parametersEntity.setId( parameterDTO.getId() );
        parametersEntity.setDescription( parameterDTO.getDescription() );

        return parametersEntity;
    }

    @Override
    public List<ParametersDTO> listParamatersEntityToDTO(List<ParametersEntity> parametersEntities) {
        if ( parametersEntities == null ) {
            return null;
        }

        List<ParametersDTO> list = new ArrayList<ParametersDTO>( parametersEntities.size() );
        for ( ParametersEntity parametersEntity : parametersEntities ) {
            list.add( parameterEntityToDTO( parametersEntity ) );
        }

        return list;
    }

    @Override
    public List<ParametersEntity> listParamatersDTOToEntity(List<ParametersDTO> calendarDTOs) {
        if ( calendarDTOs == null ) {
            return null;
        }

        List<ParametersEntity> list = new ArrayList<ParametersEntity>( calendarDTOs.size() );
        for ( ParametersDTO parametersDTO : calendarDTOs ) {
            list.add( parameterDTOToEntity( parametersDTO ) );
        }

        return list;
    }
}
