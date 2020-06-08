package dania.app.web.service;

import dania.app.web.controllers.dto.UserDTO;
import dania.app.web.mapper.UserMapper;
import dania.app.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements ServiceManager<UserDTO, Integer> {

    private UserRepository userRepository;
    private UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        return userMapper.userEntityToDTO(userRepository.saveAndFlush(userMapper.userDTOToEntity(userDTO)));
    }

    @Override
    public UserDTO delete(Integer id) {
        UserDTO userDTO = findById(id);
        if (userDTO != null) {
            userRepository.delete(userMapper.userDTOToEntity(userDTO));
        }
        return userDTO;
    }

    @Override
    public List<UserDTO> findAll() {
        return userMapper.listUserEntityToDTO(userRepository.findAll());
    }

    @Override
    public UserDTO findById(Integer id) {
        return userMapper.userEntityToDTO(userRepository.findById(id));
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        return userMapper.userEntityToDTO(userRepository.saveAndFlush(userMapper.userDTOToEntity(userDTO)));
    }
}
