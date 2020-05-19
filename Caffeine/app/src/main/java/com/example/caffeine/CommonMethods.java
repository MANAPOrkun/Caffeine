package com.example.caffeine;

import android.app.Activity;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class CommonMethods {

    private static UserClass user = new UserClass();

    static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    static boolean isValidPassword(String password){
        return password.length() == 0 || password.length() < 6 || password.length() > 24;
    }

    static void toast_msg(String msg, Activity context){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static String ShowData(Cursor cursor){
        StringBuilder text = new StringBuilder();
        while (cursor.moveToNext())
        {
            text.append(cursor.getString(0)).append(",")
                    .append(cursor.getString(1)).append(",")
                    .append(cursor.getString(2)).append(",")
                    .append(cursor.getString(3)).append("\n");
        }

        return text.toString();

    }

    static UserClass returnUser(String uid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user.setId(uid);

        db.collection("User").whereEqualTo("id", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document :
                                    Objects.requireNonNull(task.getResult())) {
                                user.setName(document.getString("name"));
                                user.setEmail(document.getString("email"));
                                user.setPassword(document.getString("password"));
                                user.setPhone(document.getString("phone"));
                                user.setImgUrl(document.getString("imgUrl"));

                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return user;
    }



}
