package ru.itis.resourcemanagement.dto;

import lombok.*;
import ru.itis.resourcemanagement.dto.projections.TeamInfo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamResponse implements TeamInfo {

    private Long id;
    private String name;
    private UserBriefDto supervisor;
}
