package com.mdgspace.activityleaderboard.payload.request;

import java.io.Serializable;
import java.util.Map;


public class ChangeOrgMembersStatusRequest implements Serializable{
    
    private Map<String , String> orgMembersStatus;

    public ChangeOrgMembersStatusRequest(){

    }

    public ChangeOrgMembersStatusRequest(Map<String,String> orgMembersStatus){
      this.orgMembersStatus=orgMembersStatus;
    }

    public Map<String , String> getOrgMembersStatus(){
        return orgMembersStatus;
    }

    public void setOrgMembersStatus(Map<String,String> orgMemberStatus){
          this.orgMembersStatus=orgMemberStatus;
    }

}
