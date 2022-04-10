package ru.itis.resourcemanagement.dto;

import lombok.*;
import ru.itis.resourcemanagement.dto.projections.ProjectInfo;
import ru.itis.resourcemanagement.dto.projections.TaskListInfo;
import ru.itis.resourcemanagement.dto.projections.TeamInfo;
import ru.itis.resourcemanagement.model.Position;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBriefDto implements TeamInfo.UserInfo, TaskListInfo.UserInfo, ProjectInfo.UserInfo {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private Position position;
}
