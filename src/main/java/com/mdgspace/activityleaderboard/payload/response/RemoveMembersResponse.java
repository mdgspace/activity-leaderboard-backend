package com.mdgspace.activityleaderboard.payload.response;

import java.io.Serializable;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class RemoveMembersResponse {
    
    private Set<String> membersRemoved;
}
