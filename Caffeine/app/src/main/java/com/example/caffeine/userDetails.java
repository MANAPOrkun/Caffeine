package com.example.caffeine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class userDetails extends AppCompatActivity {

    private View decorView;

    EditText name,email,password_1,password_2;
    ImageView ivProfile;
    String user_uid;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    UserClass userClass;

    ProgressBar progressBar;

    UserDatabaseClass user_db;

    private static final String TAG = "userDetailsActivity";

    Dialog positive_dialog;
    Button positive_button;
    ImageButton positive_close;

    private StorageReference mStorageRef;

    private StorageTask storageTask;

    public Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.etName);
        email = findViewById(R.id.etEmail);
        password_1 = findViewById(R.id.etPassword);
        password_2 = findViewById(R.id.etPassword2);
        ivProfile = findViewById(R.id.ivImage);

        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        mStorageRef = FirebaseStorage.getInstance().getReference("UserProfileImage");

        positive_dialog = new Dialog(this);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser currentUser) {

        if(currentUser != null){
            user_uid = currentUser.getUid();
            userClass = getCurrentUser();
            assert userClass != null;
            name.setText(userClass.getName());
            email.setText(userClass.getEmail());
            Glide.with(getApplicationContext())
                    .load(userClass.getImgUrl())
                    .into(ivProfile);
        }
    }


    public void onClickUpdate(View view) {

        progressBar.setVisibility(View.VISIBLE);

        if(storageTask != null && storageTask.isInProgress()){
            Toast.makeText(userDetails.this, "Update in progress",
                    Toast.LENGTH_SHORT).show();
        }
        else{

            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            updateUser(db);
        }
        progressBar.setVisibility(View.GONE);

    }

    @SuppressLint("SetTextI18n")
    private void updateUser(final FirebaseFirestore db){
        final String name_text = name.getText().toString();
        final String email_text = email.getText().toString();
        final String password_text = password_1.getText().toString();
        final String verification_password = password_2.getText().toString();
        final String id = userClass.getId();

        positive_dialog.setContentView(R.layout.popup_positive);
        positive_close = (ImageButton) positive_dialog.findViewById(R.id.ibGo);
        positive_button = (Button) positive_dialog.findViewById(R.id.buttonCart);
        positive_button.setText("OK");

        positive_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { positive_dialog.dismiss(); }
        });

        positive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positive_dialog.dismiss();
            }
        });

        if(signInVerification()) {

            db.collection("User")
                    .whereEqualTo("email", email_text)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !email_text.equals(userClass.getEmail())) {
                                for (QueryDocumentSnapshot document :
                                        Objects.requireNonNull(task.getResult())) {
                                    email.setError("This email is already taken. " +
                                            "Please enter a different email address.");
                                    email.requestFocus();
                                }
                            } else {
                                if(verification_password.equals(password_text)){

                                    db.collection("User")
                                            .document(id).update("Name", name_text)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                        }
                                    });
                                    db.collection("User")
                                            .document(id).update("Email", email_text).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                        }
                                    });
                                    db.collection("User")
                                            .document(id).update("Password", password_text).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                        }
                                    });

                                    updateImage(db);
                                }
                                else{
                                    password_2.setError("Passwords must be same.");
                                    password_2.requestFocus();
                                    password_1.requestFocus();
                                }
                            }
                        }
                    });

            progressBar.setVisibility(View.GONE);
        }
    }

    private UserClass getCurrentUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            return CommonMethods.returnUser(uid);
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    private boolean signInVerification(){

        String user_name = name.getText().toString();
        Editable mail = email.getText();
        Editable passwordText = password_1.getText();
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
            password_1.setError("Please enter a password!");
            password_1.requestFocus();
        }
        else if(password_2.getText().toString().isEmpty()){
            password_2.setError("Please enter a validation password!");
            password_2.requestFocus();
        }
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
            else if(CommonMethods.isValidPassword(passwordText.toString())){
                password_1.setError("Please enter a valid password. " +
                        "A valid password must include between 6 - 24 characters");
                password_1.requestFocus();
            }
//            else if(user_db.checkUserEmail(mail.toString())
//                    && !userClass.getEmail().equals(mail.toString())){
//                email.setError("This email is already taken. " +
//                        "Please enter a different email address.");
//                email.requestFocus();
//            }
//            else if(!user_db.insertData(user_name, mail.toString(),
//                    passwordText.toString(),"null"))
//            {
//                CommonMethods.toast_msg("Error", this);
//            }
            else
                return true;
        }
        return false;
    }

    private void updateImage(final FirebaseFirestore db){
        final String user_uid = userClass.getId();
        final StorageReference reference = mStorageRef.child(user_uid+"."+getExtension(imgUri));

        final String id = userClass.getId();

        storageTask = reference.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                db.collection("User").document(id).update("imgUrl", uri.toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                                positive_dialog.getWindow()
                                                        .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                positive_dialog.show();
                                                CommonMethods.toast_msg("Successful", userDetails.this);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error writing document", e);
                                            }
                                        });
                            }
                        });
                        Toast.makeText(userDetails.this,
                                "Image uploaded successfully",Toast.LENGTH_SHORT).show();
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(userDetails.this,
                                "Image failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void addImageClick(View view) {
        fileChooser();
    }

    private void fileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null
                && data.getData() != null){
            imgUri = data.getData();
            Glide.with(getApplicationContext())
                    .load(imgUri)
                    .into(ivProfile);
        }
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
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

    public void onBackClick(View view) {
        finish();
    }

}
