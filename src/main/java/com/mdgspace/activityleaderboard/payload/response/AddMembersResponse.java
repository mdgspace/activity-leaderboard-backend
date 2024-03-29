package com.mdgspace.activityleaderboard.payload.response;

import java.io.Serializable;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AddMembersResponse  implements Serializable{
    
    private Set<String> membersAddedOrRemoved;
}
