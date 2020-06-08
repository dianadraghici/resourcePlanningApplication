package dania.app.web.mapper;

import dania.app.web.controllers.dto.UserDTO;
import dania.app.web.entities.UserEntity;
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
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO userEntityToDTO(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( userEntity.getId() );
        userDTO.setEmail( userEntity.getEmail() );
        userDTO.setPassword( userEntity.getPassword() );

        return userDTO;
    }

    @Override
    public UserEntity userDTOToEntity(UserDTO UserDTO) {
        if ( UserDTO == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( UserDTO.getId() );
        userEntity.setEmail( UserDTO.getEmail() );
        userEntity.setPassword( UserDTO.getPassword() );

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
