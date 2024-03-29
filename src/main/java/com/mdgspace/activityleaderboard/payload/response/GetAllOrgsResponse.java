package com.mdgspace.activityleaderboard.payload.response;

import java.io.Serializable;
import java.util.List;

import com.mdgspace.activityleaderboard.models.Organization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetAllOrgsResponse implements Serializable{
    private List<Organization> organizations;
}
