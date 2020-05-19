package com.example.caffeine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private static final String TAG = "RegisterActivity";

    private View decorView;
    UserDatabaseClass user_db;
    EditText name, email, password,phone;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });


        user_db = new UserDatabaseClass(this);

        name = findViewById(R.id.etName);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        //phone = findViewById(R.id.etPhone);

        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    private int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    public void onClick(View view) {
        finish();
    }

    public void onClickRegister(View view) {

        if(!signInVerification()) return;
        createAccount(email.getText().toString(),password.getText().toString());

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount(final String email_text, final String password_text){
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email_text, password_text)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserClass new_user = new UserClass(
                                    name.getText().toString(),
                                    email_text,
                                    password_text,
                                    "null",//phone.getText().toString(),
                                    user.getUid(),"not_uploaded");

                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            db.collection("User")
                                    .add(new_user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this
                                    , "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user == null) return;
        CommonMethods.toast_msg("Registered successfully",this);
        user.sendEmailVerification();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private boolean signInVerification(){

        String user_name = name.getText().toString();
        Editable mail = email.getText();
        Editable passwordText = password.getText();
        //String phoneNumber = phone.getText().toString();

        if(user_name.isEmpty()){
            name.setError("Please enter a name!");
            name.requestFocus();
        }
        else if(mail.toString().isEmpty()){
            email.setError("Please enter an email!");
            email.requestFocus();
        }
        else if(passwordText.toString().isEmpty()){
            password.setError("Please enter a password!");
            password.requestFocus();
        }
//        else if(phoneNumber.isEmpty()){
//            phone.setError("Please enter a phone number!");
//            phone.requestFocus();
//        }
        else{
            if(!CommonMethods.isValidEmail(mail)){
                email.setError("Please enter a valid email!");
                email.requestFocus();
            }
            else if(user_name.length()<4){
                name.setError("Please enter a valid name! " +
                        "A valid name must include at least 4 characters");
                name.requestFocus();
            }
            else if(!CommonMethods.isValidPassword(password.toString())){
                password.setError("Please enter a valid password. " +
                        "A valid password must include between 6 - 24 characters");
                password.requestFocus();
            }
            else if(user_db.checkUserEmail(mail.toString())){
                email.setError("This email is already taken. " +
                        "Please enter a different email address.");
                email.requestFocus();
            }
            else if(!user_db.insertData(user_name, mail.toString(),
                    passwordText.toString(),"null"))
            {
                CommonMethods.toast_msg("Error", this);
            }
            else
                return true;
        }
        return false;
    }
}
