package com.example.free;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.format.DateTimeFormatter;

import static android.content.ContentValues.TAG;

public class LookUsersActivity extends AppCompatActivity {
    public class userCallback implements Database.createUserCallback{
        @Override
        public void onCallback(final User u){
            for(String friendUserName : u.getFriendListString()){
                Database.getUser(friendUserName, new Database.createUserCallback(){
                    @Override
                    public void onCallback(User friend){
                        Block intersect = u.freeWith(friend);
                        if(intersect != null){
                            pushToScreen(friend, intersect);
                        }
                    }
                });
            }
        }
    }

    private void pushToScreen(User friend, Block b){
        LinearLayout userLayout = findViewById(R.id.linear_layout);
        TextView userView = new TextView(this);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        String endTime = b.endDT.format(formatter);
            userView.setText(
                      "\n"
                    + friend.getUserName()
                    + ":  \""
                    + friend.freeTime.getMessage()
                    + "\"\navailable until: "
                    + endTime
                    + " for "
                    + b.freeDuration.toHours()
                    + " hours and "
                    + b.freeDuration.minusHours(b.freeDuration.toHours()).toMinutes() + " minutes \n"
                    );
            userLayout.addView(userView);
    }

    private void displayFriends(){
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        Database.getUser(fbUser.getDisplayName(), new userCallback());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_users);

        displayFriends();
    }
}
