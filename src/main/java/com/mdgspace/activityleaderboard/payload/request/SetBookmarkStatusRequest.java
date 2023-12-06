package com.mdgspace.activityleaderboard.payload.request;



import java.util.Map;


public class SetBookmarkStatusRequest {
    
    private Map<String , Boolean> bookmarkStatus;

    public SetBookmarkStatusRequest(){

    }

    public SetBookmarkStatusRequest(Map<String , Boolean> bookmarkStatus){
        this.bookmarkStatus=bookmarkStatus;
    }

    public Map<String , Boolean> getBookmarkStatus(){
        return bookmarkStatus;
    }


    public void  setBookMarkStatus(Map<String, Boolean> bookmarkStatus){

        this.bookmarkStatus=bookmarkStatus;
        
    }

}
