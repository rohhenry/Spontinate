package com.example.free;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    public static final String HOURS_FREE = "com.example.free.HOURS_FREE";
//    public static final String MINUTES_FREE = "com.example.free.MINUTES_FREE";
//    public static final String MESSAGE = "com.example.free.MESSAGE";

    TimePicker picker;
    EditText editText;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText= findViewById(R.id.editText);
        picker= findViewById(R.id.timePicker1);
        picker.setIs24HourView(true);

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        user = new User(fbUser.getDisplayName());

    }

    public void generateTestUsers(View view){
        //FAKE DATA
        User friendA = new User("friendA");
        User friendB = new User("friendB");
        friendA.makeFree(2);
        friendB.makeFree(5);
        friendA.freeTime.setMessage("Anyone down for some csgo?");
        friendB.freeTime.setMessage("I'm so bored...");

        Database db = new Database();
        db.addUser(friendA);
        db.addUser(friendB);

        db.setFree(friendA);
        db.setFree(friendB);
        db.addFriend(user.getUserName(), friendA.getUserName());
        db.addFriend(user.getUserName(), friendB.getUserName());

        User friendC = new User("friendC");
        friendC.makeFree(10);
        friendC.freeTime.setMessage("Can someone teach me how to avoid callback hell");
        db.addUser(friendC);
        db.setFree(friendC);
    }


    public void addFriend(View view){
        Intent intent = new Intent(this, addFriendActivity.class);
        startActivity(intent);
    }

    public void signOut(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }


    /** Called when user clicks Free button **/
    public void makeFree(View view){
        Intent intent = new Intent(this, LookUsersActivity.class);

        user.makeFree(picker.getHour(), picker.getMinute());
        user.freeTime.setMessage(editText.getText().toString());

        Database db = new Database();
        db.addUser(user);
        db.setFree(user);

//        try {
//            intent.putExtra(HOURS_FREE, picker.getHour());
//            intent.putExtra(MINUTES_FREE, picker.getMinute());
//            intent.putExtra(MESSAGE, editText.getText().toString());
//            startActivity(intent);
//        } catch (NumberFormatException e) {
//            return;
//        }
        startActivity(intent);
    }
}
