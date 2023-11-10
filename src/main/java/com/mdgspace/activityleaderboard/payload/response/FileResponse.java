package com.mdgspace.activityleaderboard.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class FileResponse {
    private String message;
    private boolean isSuccessful;
    private int statusCode;
    private Object data;
}
