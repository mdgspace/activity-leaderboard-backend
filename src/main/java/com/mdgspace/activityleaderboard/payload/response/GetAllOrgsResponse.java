package com.mdgspace.activityleaderboard.payload.response;

import java.util.List;

import com.mdgspace.activityleaderboard.models.Organization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class GetAllOrgsResponse {
    private List<Organization> organizations;
}