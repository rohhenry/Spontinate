package com.example.free;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userName;
    Block freeTime;

    private List<User> friendList;
    private List<String> friendListString;

    public User(String userName){
        this.userName = userName;
        friendList = new ArrayList<>();
        friendList = new ArrayList<>();
    }

    public void makeFree(long hourLength){
        freeTime = new Block(hourLength);
    }

    public void makeFree(int hour, int minute){
        freeTime = new Block(hour, minute);
    }

    public List<Result> findFree(List<User> userList){
        List<Result> availableUsers = new ArrayList<Result>();
        for(User u: userList){
            Block b = freeTime.intersectionWith(u.freeTime);
            if(b != null){
                availableUsers.add(new Result(u, b));
            }
        }
        return availableUsers;
    }

    public Block freeWith(User friend){
        return freeTime.intersectionWith(friend.freeTime);
    }

    public void setFriendList(List<User> friendList){
        this.friendList = friendList;
    }

    public void setFreeTime(Block freeTime){
        this.freeTime = freeTime;
    }

    public String getUserName() {
        return userName;
    }

    public List<User> getFriendList(){
        return friendList;
    }

    public List<String> getFriendListString(){
        return friendListString;
    }

    @Override
    public String toString(){
        return userName;
    }

    public void setFriendListString(List<String> friendListString) {
        this.friendListString = friendListString;
    }
}
