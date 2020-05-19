package com.example.caffeine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private View decorView;

    UserDatabaseClass db;
    EditText email, password;
    Button login;
    Switch save_switch;

    public TextView signUp;

    public static final String shared_prefs = "sharedPrefs";
    public static final String saved_mail = "";
    public static final String switch1 = "switch1";

    private String email_text;
    private boolean switchOnOff;

    private boolean logged;

    private FirebaseAuth mAuth;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUp = findViewById(R.id.tvSignUp);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        mAuth = FirebaseAuth.getInstance();

        db = new UserDatabaseClass(this);

        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        login = findViewById(R.id.btnSignIn);
        save_switch = findViewById(R.id.switch_save);

        logged = false;

        loadData();
        updateViews();

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

    public void monClick(View view) {
        Intent register = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(register);
    }


    @SuppressLint("SetTextI18n")
    public void onClickLogin(View view) {
        if(!checkInformation()) return;
        signIn(email.getText().toString(),password.getText().toString());
    }
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(shared_prefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(saved_mail, email.getText().toString());
        editor.putBoolean(switch1, save_switch.isChecked());

        editor.apply();

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(shared_prefs, MODE_PRIVATE);
        email_text = sharedPreferences.getString(saved_mail, "");
        switchOnOff = sharedPreferences.getBoolean(switch1, false);
    }

    public void updateViews() {
        email.setText(email_text);
        save_switch.setChecked(switchOnOff);
    }

    public void deleteData(){
        SharedPreferences sharedPreferences = getSharedPreferences(shared_prefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();

        editor.putString(saved_mail, "");
        editor.putBoolean(switch1, false);

        editor.apply();
    }

    @SuppressLint("SetTextI18n")
    public void onClickLogout(View view) {
        if(logged){
            logged = false;
        }
        else{
            CommonMethods.toast_msg("You've already logged out.",this);
        }
    }

    public void Back(View view) {
        finish();
    }

    private void signIn(String email_text, String password_text){
        mAuth.signInWithEmailAndPassword(email_text, password_text)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user == null) {
            CommonMethods.toast_msg("Sign in failed. Please try again.", this);
        }
        else if(user.isEmailVerified()){
            login.setError("Please verify your email first.");
            login.requestFocus();
        }
        else{
            Editable mail = email.getText();
            boolean checked = save_switch.isChecked();

            if (checked) {
                saveData();
            }
            else{
                deleteData();
            }

            CommonMethods.toast_msg("Logged in successfully.", this);
            logged = true;
            Intent login = new Intent (MainActivity.this, HomeActivity.class);
            login.putExtra("logged", logged);
            login.putExtra("email", mail.toString());
            startActivity(login);

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent profile =
                    new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(profile);
        }

    }

    private boolean checkInformation(){
        if(email.getText().toString().length() == 0){
            email.setError("Please enter your email first.");
            email.requestFocus();
            return false;
        }
        else if(password.getText().toString().length() == 0){
            password.setError("Please enter your password.");
            password.requestFocus();
            return false;
        }
        return true;
    }
}


