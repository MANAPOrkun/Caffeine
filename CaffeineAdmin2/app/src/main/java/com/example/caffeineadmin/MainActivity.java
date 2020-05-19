package com.example.caffeineadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public Uri imgUri;

    EditText name,price,type,acidity,roast,flavor,origin;
    ImageView ivProduct;
    Button button;
    private ProgressDialog progressDialog;

    private StorageReference mStorageRef;

    private StorageTask storageTask;

    CoffeeBeansClass beansClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.etName);
        price = findViewById(R.id.etPrice);
        type = findViewById(R.id.etType);
        acidity = findViewById(R.id.etAcidity);
        roast = findViewById(R.id.etRoast);
        flavor = findViewById(R.id.etFlavor);
        origin = findViewById(R.id.etOrigin);
        button = findViewById(R.id.button);
        ivProduct = findViewById(R.id.ivProduct);

        mStorageRef = FirebaseStorage.getInstance().getReference("Image");

        progressDialog = new ProgressDialog(this);
    }

    private void addBeans(FirebaseFirestore db, String img_url){

        final String name_text = name.getText().toString();
        final int price_text = Integer.parseInt(price.getText().toString());
        final String type_text = type.getText().toString();
        final String acidity_text = acidity.getText().toString();
        final String roast_text = roast.getText().toString();
        final String flavor_text = flavor.getText().toString();
        final String origin_text = origin.getText().toString();



        beansClass = new CoffeeBeansClass(name_text, price_text, type_text,
                acidity_text, roast_text, flavor_text, origin_text,img_url);


        db.collection("Beans")
                .add(beansClass)
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

    }

    public void addBeanClick(View view) {
        if(storageTask != null && storageTask.isInProgress()){
            Toast.makeText(MainActivity.this, "Upload in progress",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.setMessage("Adding");
            progressDialog.show();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            //addBeans(db);
            uploadImage(db);
            progressDialog.cancel();
        }

    }

    private void uploadImage(final FirebaseFirestore db){
        final String product_name = name.getText().toString();
        final StorageReference reference = mStorageRef.child(product_name+"."+getExtension(imgUri));
        String url;

        storageTask = reference.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                addBeans(db, uri.toString());
                            }
                        });
                        Toast.makeText(MainActivity.this,
                                "Image uploaded successfully",Toast.LENGTH_SHORT).show();
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(MainActivity.this,
                                "Image failed",Toast.LENGTH_SHORT).show();
                    }
                });
//        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
//        {
//            @Override
//            public void onSuccess(Uri downloadUrl)
//            {
//                addBeans(db, downloadUrl.toString());
//            }
//        });
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
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
            ivProduct.setImageURI(imgUri);
        }
    }
}
