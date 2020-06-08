package dania.app.web.unitTests;

import dania.app.web.controllers.dto.UserDTO;
import dania.app.web.entities.UserEntity;
import dania.app.web.mapper.UserMapper;
import dania.app.web.repository.UserRepository;
import dania.app.web.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private UserDTO userDTOExpected;
    private UserEntity userEntityExpected;
    private UserService userService;

    @Before
    public void setupUserServiceTest(){
        userService = new UserService(userRepository, userMapper);

        userDTOExpected = new UserDTO();
        userDTOExpected.setId(11);
        userDTOExpected.setEmail("email");

        userEntityExpected = new UserEntity();
        userEntityExpected.setId(11);
        userEntityExpected.setEmail("email");

        when(userMapper.userEntityToDTO(any())).thenReturn(userDTOExpected);
        when(userMapper.userDTOToEntity(any())).thenReturn(userEntityExpected);
    }

    @Test
    public void createUserServiceTest(){
        //given
        when(userRepository.saveAndFlush(any())).thenReturn(userEntityExpected);

        //when
        UserDTO userDTO = userService.create(userDTOExpected);

        //then
        assertEquals(userDTOExpected.getId(), userDTO.getId());
        assertEquals(userDTOExpected.getEmail(), userDTO.getEmail());
        verify(userMapper).userEntityToDTO(userEntityExpected);
        verify(userRepository).saveAndFlush(userEntityExpected);
    }

    @Test
    public void deleteUserServiceTest(){
        //given
        when(userService.findById(anyInt())).thenReturn(userDTOExpected);

        //when
        UserDTO userDTO = userService.delete(anyInt());

        //then
        assertEquals(userDTOExpected.getId(), userDTO.getId());
        verify(userRepository,times(2)).findById(anyInt());
        verify(userMapper).userDTOToEntity(userDTOExpected);
    }

    @Test
    public void findAllUserServiceTest(){
        //given
        List<UserDTO> userDTOExpectedList = new ArrayList<>();
        userDTOExpectedList.add(userDTOExpected);

        List<UserEntity> userEntityExpectedList = new ArrayList<>();
        userEntityExpectedList.add(userEntityExpected);

        when(userRepository.findAll()).thenReturn(userEntityExpectedList);
        when(userMapper.listUserEntityToDTO(any())).thenReturn(userDTOExpectedList);

        //when
        List<UserDTO> userlist = userService.findAll();
        UserDTO userFromResultList = userlist.get(0);

        //then
        assertEquals(userlist.size(), userDTOExpectedList.size());
        assertEquals(userFromResultList.getId(), userDTOExpected.getId());
        assertEquals(userFromResultList.getEmail(), userDTOExpected.getEmail());
        verify(userMapper).listUserEntityToDTO(any());
        verify(userRepository).findAll();
    }

    @Test
    public void findByIdUserServiceTest(){
        //given
        when(userRepository.findById(anyInt())).thenReturn(userEntityExpected);

        //when
        UserDTO userDTO = userService.findById(anyInt());

        //then
        assertEquals(userDTOExpected.getId(), userDTO.getId());
        verify(userRepository).findById(anyInt());
        verify(userMapper).userEntityToDTO(userEntityExpected);
    }

    @Test
    public void updateUserServiceTest(){
        //given
        when(userRepository.saveAndFlush(any())).thenReturn(userEntityExpected);

        //when
        UserDTO userDTO = userService.create(userDTOExpected);

        //then
        assertEquals(userDTOExpected.getId(), userDTO.getId());
        verify(userRepository).saveAndFlush(userEntityExpected);
        verify(userMapper).userEntityToDTO(userEntityExpected);
    }
}
