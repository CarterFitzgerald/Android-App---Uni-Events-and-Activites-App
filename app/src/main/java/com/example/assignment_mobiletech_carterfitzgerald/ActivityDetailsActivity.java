package com.example.assignment_mobiletech_carterfitzgerald;

import static android.app.ProgressDialog.show;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ActivityDetailsActivity extends AppCompatActivity {

    String filenameOnCloud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        downloadMultipleValuesFromRealtimeDB();
    }

    public void downloadMultipleValuesFromRealtimeDB() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Retrieve the values for each child node
                String activityName = dataSnapshot.child("activityName").getValue(String.class);
                String title = dataSnapshot.child("title").getValue(String.class);
                String summary = dataSnapshot.child("summary").getValue(String.class);
                String times = dataSnapshot.child("times").getValue(String.class);
                String dates = dataSnapshot.child("dates").getValue(String.class);
                String contact = dataSnapshot.child("contact").getValue(String.class);
                String location = dataSnapshot.child("location").getValue(String.class);
                filenameOnCloud = dataSnapshot.child("imgFilename").getValue(String.class);

                TextView eventNameTextView = findViewById(R.id.eventName);
                TextView textView7 = findViewById(R.id.textView7);
                TextView textView8 = findViewById(R.id.textView8);
                TextView textView9 = findViewById(R.id.textView9);
                TextView textView10 = findViewById(R.id.textView10);
                TextView textView11 = findViewById(R.id.textView11);
                TextView textView12 = findViewById(R.id.textView12);

                eventNameTextView.setText(activityName);
                textView7.setText(title);
                textView8.setText(summary);
                textView9.setText(times);
                textView10.setText(dates);
                textView11.setText(contact);
                textView12.setText(location);
                //downloadSingleFilefromStorage(filenameOnCloud);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void downloadSingleFilefromStorage(String filenameOnCloud) {
        File fileOnPhone = null;
        try {
            fileOnPhone = File.
                    createTempFile(filenameOnCloud, "");
        } catch (IOException e) {
            e.printStackTrace();
        StorageReference fileRef = FirebaseStorage.
                getInstance().getReference()
                .child(filenameOnCloud);
        if (fileRef == null) return;
        if (fileOnPhone != null) {
            File finalLocalFile = fileOnPhone;
            fileRef.getFile(fileOnPhone)
                    .addOnSuccessListener(
                            new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Uri uri = Uri.
                                            fromFile(finalLocalFile);
                                    ImageView imageView = (ImageView) findViewById(R.id.imageView3);
                                    imageView.setImageURI(uri);
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(getApplicationContext(), "Unable to download ",
                                                    Toast.LENGTH_SHORT).show();
                                }
                            });
                    }
        }
    }

    public void openMapsActivity(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}