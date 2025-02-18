package com.example.assignment_mobiletech_carterfitzgerald;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UploadImageActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1001;
    private static final int REQUEST_IMAGE_CAPTURE = 1000;
    private static final int REQUEST_PERMISSION = 3000;
    private Activity activity;
    private Uri imageFileUri;
    private ImageView imageView2;

    private String activityName;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        activity = this;
        imageView2 = findViewById(R.id.imageView2);

        Intent intent = getIntent();

        if (intent != null) {
            activityName = intent.getStringExtra("activityName");
            key = intent.getStringExtra("key");
        }
    }

    public void capture(View view) {
        if(checkPermissions() == false)
            return;
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFileUri = getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
        activity.startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
    }

    public void load(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    private boolean checkPermissions() {
        String cameraPermission = android.Manifest.permission.CAMERA;
        boolean grantCamera =
                ContextCompat.checkSelfPermission(activity,
                        cameraPermission) == PackageManager.PERMISSION_GRANTED;
        if(!grantCamera) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{cameraPermission}, REQUEST_PERMISSION);
        }
        return grantCamera;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (imageFileUri != null) {
                    imageView2.setImageURI(imageFileUri);
                }
            } else if (requestCode == PICK_IMAGE_REQUEST) {
                if (data != null && data.getData() != null) {
                    imageFileUri = data.getData();
                    imageView2.setImageURI(imageFileUri);
                }
            }
        }
    }

    public interface OnImageFilenameCallback {
        void onImageFilenameReceived(String imgFilename);
    }

    public void downloadSingleValueFromRealtimeDB(String key, OnImageFilenameCallback callback) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(activityName).child(key).child("imgFilename");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String imgFilename = dataSnapshot.getValue(String.class);
                    // Pass the imgFilename to the callback method
                    callback.onImageFilenameReceived(imgFilename);
                } else {
                    // If imgFilename not found, pass null to the callback method
                    callback.onImageFilenameReceived(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting the value failed, handle the error or pass null to the callback method
                Log.w(TAG, "loadImgFilename:onCancelled", databaseError.toException());
                callback.onImageFilenameReceived(null);
            }
        });
    }

    public void uploadSingleResourceFileToCloudStorage(Uri uri, String filenameOnCloud) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child(filenameOnCloud).putFile(uri);
    }

    private void updateOrInsertActivityNameInDatabase(String newActivityName) {
        MyDbHelper dbHelper = new MyDbHelper(this, "Event_DB", null, 1);

        // Check if the activity name already exists in the database
        ArrayList<String> existingNames = dbHelper.readAll();
        boolean activityNameExists = existingNames.contains(newActivityName);

        if (activityNameExists) {
            // Update the existing row with the new activity name
            dbHelper.update(newActivityName);
        } else {
            // Create a new row with the activity name
            dbHelper.create(newActivityName);
        }
    }

    public void doneButtonClicked(View view) {
        // Call downloadSingleValueFromRealtimeDB and pass a callback
        downloadSingleValueFromRealtimeDB(key, new OnImageFilenameCallback() {
            @Override
            public void onImageFilenameReceived(String imgFilename) {
                // Check if imgFilename is not null
                if (imgFilename != null) {
                    // If imgFilename is retrieved successfully, upload the image to Cloud Storage
                    uploadSingleResourceFileToCloudStorage(imageFileUri, imgFilename);
                }
            }
        });
        MyDbHelper dbHelper = new MyDbHelper(this, "Events_DB", null, 1);
        dbHelper.updateOrInsertActivityNameInDatabase(activityName);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
