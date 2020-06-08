package dania.app.web.mapper;

import dania.app.web.controllers.dto.UserDTO;
import dania.app.web.entities.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO userEntityToDTO(UserEntity userEntity);

    UserEntity userDTOToEntity(UserDTO UserDTO);

    List<UserDTO> listUserEntityToDTO(List<UserEntity> userEntities);

    List<UserEntity> listUserDTOToEntity(List<UserDTO> UserDTOS);

}
