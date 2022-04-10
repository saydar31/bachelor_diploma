package ru.itis.resourcemanagement.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TeamDto {
    private String name;
    private Long supervisorId;
    private List<Long> userIdList;
}
