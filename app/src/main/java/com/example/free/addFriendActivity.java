package com.example.free;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class addFriendActivity extends AppCompatActivity {
    EditText friendForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        friendForm = findViewById(R.id.friendForm);
    }

    public void add(View view){
        Database db = new Database();
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

        db.addFriend(fbUser.getDisplayName(), friendForm.getText().toString());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
