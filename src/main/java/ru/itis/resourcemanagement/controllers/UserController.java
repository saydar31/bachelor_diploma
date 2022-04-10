package ru.itis.resourcemanagement.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.resourcemanagement.dto.projections.UserInfo;
import ru.itis.resourcemanagement.model.User;
import ru.itis.resourcemanagement.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/employees")
    public List<UserInfo> getEmployees() {
        return userService.getEmployees();
    }

    @GetMapping("/team-supervisors")
    public List<UserInfo> getTeamSupervisors() {
        return userService.getTeamSupervisors();
    }
}
