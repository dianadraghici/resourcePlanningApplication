package dania.app.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Map;

@Controller
public class LoginController {
    @RequestMapping("/secure")
    public String secure(Map<String, Object> model, Principal principal) {
        model.put("title", "SECURE AREA");
        model.put("message", "Only Authorized Users Can See This Page");
        return "home";
    }
}
