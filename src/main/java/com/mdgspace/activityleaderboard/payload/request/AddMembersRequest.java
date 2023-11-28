package com.mdgspace.activityleaderboard.payload.request;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class AddMembersRequest {
    
    private Set<String> members;
}
