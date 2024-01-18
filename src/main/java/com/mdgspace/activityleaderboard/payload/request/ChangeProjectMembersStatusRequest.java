package com.mdgspace.activityleaderboard.payload.request;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangeProjectMembersStatusRequest implements Serializable {
    private Map<String, String> projectMembersStatus;
}
