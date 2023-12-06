package com.mdgspace.activityleaderboard.payload.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class AddMembersResponse {
    
    private Set<String> membersAddedOrRemoved;
}
