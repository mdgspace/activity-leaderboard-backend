package com.mdgspace.activityleaderboard.payload.request;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class SetArcheiveStatusRequest {
    
    private Map<String , Boolean> archeiveStatus;

    public  Map<String, Boolean> getArcheiveStatus(){
        return archeiveStatus;
    }

    public void setArcheiveStatus(Map<String, Boolean> archieveStatus){
        this.archeiveStatus=archieveStatus;
    }
}
