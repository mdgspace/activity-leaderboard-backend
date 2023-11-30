package com.mdgspace.activityleaderboard.payload.request;



import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class SetBookmarkStatusRequest {
    
    private Map<String , Boolean> bookmarkStatus;
}