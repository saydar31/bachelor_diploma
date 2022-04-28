package ru.itis.resourcemanagement.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.resourcemanagement.dto.UserDto;
import ru.itis.resourcemanagement.dto.projections.UserInfo;
import ru.itis.resourcemanagement.model.User;
import ru.itis.resourcemanagement.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/me")
    public UserDto me(@AuthenticationPrincipal User user){
        return modelMapper.map(user, UserDto.class);
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
