package com.mdgspace.activityleaderboard.payload.response;

import java.io.Serializable;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RemoveMembersResponse implements Serializable{
    
    private Set<String> membersRemoved;
}
