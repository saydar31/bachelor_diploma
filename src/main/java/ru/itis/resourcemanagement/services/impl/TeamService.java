package ru.itis.resourcemanagement.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.itis.resourcemanagement.dto.UserDto;
import ru.itis.resourcemanagement.dto.projections.TeamInfo;
import ru.itis.resourcemanagement.exceptions.NotFoundException;
import ru.itis.resourcemanagement.model.Team;
import ru.itis.resourcemanagement.repositories.TeamRepository;
import ru.itis.resourcemanagement.services.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TeamService {

    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;

    public TeamService(TeamRepository teamRepository, UserService userService, ModelMapper modelMapper) {
        this.teamRepository = teamRepository;
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
}
