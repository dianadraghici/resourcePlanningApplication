package dania.app.web.controllers;

import dania.app.web.controllers.dto.UserDTO;
import dania.app.web.service.UserService;
import dania.app.web.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({Constants.USER_CONTROLLER})
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    List<UserDTO> getListUser() {
        return new ArrayList<>(userService.findAll());
    }
}
