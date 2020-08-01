package com.example.free;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Database {
    FirebaseFirestore db;

    public Database(){
        db = FirebaseFirestore.getInstance();
    }

    public void addUser(User u){
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", u.getUserName());
        userData.put("password", "testPass");
        //userData.put("friendList", u.getFriendListString());
        db.collection("users").document(u.getUserName())
                .set(userData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public void addFriend(final String user1, final String user2){
        DocumentReference docRef = db.collection("users").document(user2);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        db.collection("users").document(user1).update("friendList", FieldValue.arrayUnion(user2));
                        db.collection("users").document(user2).update("friendList", FieldValue.arrayUnion(user1));

                    } else {
                        Log.d(TAG, "No one with user name");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void setFree(User user){
        Map<String, Object> post = new HashMap<>();
        post.put("startDT", user.freeTime.getStartDT().toString());
        post.put("endDT", user.freeTime.getEndDT().toString());
        post.put("message", user.freeTime.getMessage());

        db.collection("users").document(user.getUserName())
                .set(post, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });


    }

    public interface createUserCallback{
        void onCallback(User u);
    }

    public static void getUser(String username, final createUserCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        User u = new User(document.getString("username"));

                        LocalDateTime startDT = LocalDateTime.parse(document.getString("startDT"));
                        LocalDateTime endDT = LocalDateTime.parse(document.getString("endDT"));

                        Block b = new Block(startDT, endDT);
                        b.setMessage(document.getString("message"));
                        u.setFreeTime(b);

                        u.setFriendListString((List<String>) document.get("friendList"));

                        callback.onCallback(u);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    //public interface friendCallback(){}

//    public getFriends(User u) {
//        for (User friend : u.getFriendList()) {
//            getUser(friend.getUserName(), new Database.createUserCallback() {
//                @Override
//                public void onCallback(User friend) {
//
//                }
//            });
//        }
//        return null;
//    }
}
