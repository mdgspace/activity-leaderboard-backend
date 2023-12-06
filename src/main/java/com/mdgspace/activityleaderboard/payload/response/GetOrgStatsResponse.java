package com.mdgspace.activityleaderboard.payload.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class GetOrgStatsResponse {
    private Map<String, Map<String, Integer>> projects;
}
