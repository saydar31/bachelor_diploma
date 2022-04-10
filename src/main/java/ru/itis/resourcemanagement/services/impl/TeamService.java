package ru.itis.resourcemanagement.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.itis.resourcemanagement.dto.TeamDto;
import ru.itis.resourcemanagement.dto.TeamResponse;
import ru.itis.resourcemanagement.dto.UserDto;
import ru.itis.resourcemanagement.dto.projections.TeamInfo;
import ru.itis.resourcemanagement.exceptions.BadRequestException;
import ru.itis.resourcemanagement.exceptions.NotFoundException;
import ru.itis.resourcemanagement.model.Position;
import ru.itis.resourcemanagement.model.Team;
import ru.itis.resourcemanagement.model.User;
import ru.itis.resourcemanagement.repositories.TeamRepository;
import ru.itis.resourcemanagement.services.UserService;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public TeamService(TeamRepository teamRepository, UserService userService, ModelMapper modelMapper) {
        this.teamRepository = teamRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public List<TeamInfo> getTeams() {
        return teamRepository.getAll();
    }

    public Optional<TeamInfo> getTeam(Long id) {
        return teamRepository.findTeamById(id);
    }

    @Transactional
    public List<UserDto> getTeamMembers(Long id) {
        Optional<Team> teamOptional = teamRepository.findById(id);
        Team team = teamOptional.orElseThrow(NotFoundException::new);
        return team.getMembers()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toUnmodifiableList());
    }

    public List<UserDto> getAvailableMembers(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        List<User> users = userService.findNewAvailableUsers(team);
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public TeamInfo updateTeam(Long id, TeamDto teamDto, User user) {
        Team team = teamRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        List<User> members = userService.findByIdIn(teamDto.getUserIdList());
        if (!isAllisEmployee(members)){
            throw new BadRequestException("members", "NON_EMPLOYEE_FOUND");
        }
        team.setMembers(members);
        members.forEach(member -> member.getTeams().add(team));
        team.setName(teamDto.getName());
        return modelMapper.map(team, TeamResponse.class);
    }

    private boolean isAllisEmployee(Collection<User> users){
        return users.stream()
                .allMatch(u -> Position.EMPLOYEE.equals(u.getPosition()));
    }
}
