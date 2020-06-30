package dania.app.web.mapper;

import dania.app.web.controllers.dto.UserDTO;
import dania.app.web.entities.UserEntity;
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
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO userEntityToDTO(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        return userDTO;
    }

    @Override
    public UserEntity userDTOToEntity(UserDTO UserDTO) {
        if ( UserDTO == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        return userEntity;
    }

    @Override
    public List<UserDTO> listUserEntityToDTO(List<UserEntity> userEntities) {
        if ( userEntities == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( userEntities.size() );
        for ( UserEntity userEntity : userEntities ) {
            list.add( userEntityToDTO( userEntity ) );
        }

        return list;
    }

    @Override
    public List<UserEntity> listUserDTOToEntity(List<UserDTO> UserDTOS) {
        if ( UserDTOS == null ) {
            return null;
        }

        List<UserEntity> list = new ArrayList<UserEntity>( UserDTOS.size() );
        for ( UserDTO userDTO : UserDTOS ) {
            list.add( userDTOToEntity( userDTO ) );
        }

        return list;
    }
}
