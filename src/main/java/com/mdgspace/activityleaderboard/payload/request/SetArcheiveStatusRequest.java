package com.mdgspace.activityleaderboard.payload.request;

import java.io.Serializable;
import java.util.Map;



public class SetArcheiveStatusRequest  implements Serializable{
    
    private Map<String , Boolean> archeiveStatus;

    public SetArcheiveStatusRequest(){

    }

    public SetArcheiveStatusRequest(Map<String , Boolean> archeiveStatus){
          this.archeiveStatus=archeiveStatus;
    }

    public  Map<String, Boolean> getArcheiveStatus(){
        return archeiveStatus;
    }

    public void setArcheiveStatus(Map<String, Boolean> archieveStatus){
        this.archeiveStatus=archieveStatus;
    }
}
