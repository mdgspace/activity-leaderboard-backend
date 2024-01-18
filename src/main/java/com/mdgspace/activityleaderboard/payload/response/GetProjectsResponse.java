package com.mdgspace.activityleaderboard.payload.response;

import java.io.Serializable;
import java.util.Map;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetProjectsResponse implements Serializable{
    private Map<String,Map<String,Boolean>> projects;
}
