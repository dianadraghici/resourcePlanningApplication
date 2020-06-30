package dania.app.web.mapper;

import dania.app.web.controllers.dto.ParametersDTO;
import dania.app.web.entities.ParametersEntity;
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
public class ParametersMapperImpl implements ParametersMapper {

    @Override
    public ParametersDTO parameterEntityToDTO(ParametersEntity parameterEntity) {
        if ( parameterEntity == null ) {
            return null;
        }

        ParametersDTO parametersDTO = new ParametersDTO();

        return parametersDTO;
    }

    @Override
    public ParametersEntity parameterDTOToEntity(ParametersDTO parameterDTO) {
        if ( parameterDTO == null ) {
            return null;
        }

        ParametersEntity parametersEntity = new ParametersEntity();

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
