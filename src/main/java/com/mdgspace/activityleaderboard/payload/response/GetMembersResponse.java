package com.mdgspace.activityleaderboard.payload.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class GetMembersResponse {
    private Map<String,String> members;
}