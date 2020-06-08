package dania.app.web.controllers;

import dania.app.web.utils.Constants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.MEMBER_CONTROLLER)
public class HealthController {

    @GetMapping(value = Constants.HEALTH)
    public String checkBackendHealth() {
        return "is alive";
    }
}
