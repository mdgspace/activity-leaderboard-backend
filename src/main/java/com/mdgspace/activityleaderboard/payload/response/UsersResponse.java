package com.mdgspace.activityleaderboard.payload.response;

import java.util.List;

import com.mdgspace.activityleaderboard.models.User;

public class UsersResponse {

    private List<User> users;

    public UsersResponse(){

    }

    public UsersResponse(List<User> users){
        this.users=users;

    }

    public List<User>  getUsers(){
        return users;
    }

    public void setUsers(List<User> users){
        this.users=users;
    }
}
