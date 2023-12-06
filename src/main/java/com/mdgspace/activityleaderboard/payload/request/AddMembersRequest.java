package com.mdgspace.activityleaderboard.payload.request;

import java.util.Set;



public class AddMembersRequest {
    
    private Set<String> members;

    public AddMembersRequest(){

    }
    public AddMembersRequest(Set<String> members){
        this.members=members;
    }

   public Set<String> getMembers(){
        return members;
    }

    public void setMembers(Set<String> members){
        this.members=members;
    }
}
