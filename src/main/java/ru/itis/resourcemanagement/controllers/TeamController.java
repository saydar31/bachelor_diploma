package ru.itis.resourcemanagement.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.itis.resourcemanagement.dto.TeamDto;
import ru.itis.resourcemanagement.dto.UserDto;
import ru.itis.resourcemanagement.dto.projections.TeamInfo;
import ru.itis.resourcemanagement.dto.projections.UserInfo;
import ru.itis.resourcemanagement.model.Team;
import ru.itis.resourcemanagement.model.User;
import ru.itis.resourcemanagement.services.impl.TeamService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<List<TeamInfo>> getTeams() {
        return ResponseEntity.ok(teamService.getTeams());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PROJECT_SUPERVISOR')")
    public ResponseEntity<TeamInfo> createTeam(@RequestBody TeamDto teamDto){
        return ResponseEntity.ok(teamService.createTeam(teamDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamInfo> getTeam(@PathVariable Long id) {
        return ResponseEntity.of(teamService.getTeam(id));
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('PROJECT_SUPERVISOR')")
    public ResponseEntity<TeamInfo> updateTeam(@PathVariable Long id, @RequestBody TeamDto teamDto, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(teamService.updateTeam(id, teamDto, user));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<UserDto>> getMembers(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamMembers(id));
    }

    @GetMapping("/{id}/availableUsers")
    public ResponseEntity<List<UserInfo>> getAvailableUsers(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getAvailableMembers(id));
    }
}
