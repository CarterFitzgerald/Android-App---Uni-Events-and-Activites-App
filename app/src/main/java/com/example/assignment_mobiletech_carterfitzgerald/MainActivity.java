package com.example.assignment_mobiletech_carterfitzgerald;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    private EditText editTextActivityName;
    private MyDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new MyDbHelper(this, "Events_DB", null, 1);

        setContentView(R.layout.activity_main);

        editTextActivityName = findViewById(R.id.activityname);

        // Retrieve activity name from the database
        String activityName = getActivityNameFromDatabase();

        // Prefill the EditText with the activity name
        editTextActivityName.setText(activityName);
    }

    private String getActivityNameFromDatabase() {
        return dbHelper.getName();
    }

    public void openAddEventActivity(View view) {
        EditText editText = findViewById(R.id.activityname);
        String activityName = editText.getText().toString();

        Intent intent = new Intent(this, AddEventActivity.class);
        intent.putExtra("ACTIVITY_NAME", activityName);
        startActivity(intent);
    }

    public void openMapsActivity(View view) {
        EditText editText = findViewById(R.id.activityname);
        String activityName = editText.getText().toString();
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("ACTIVITY_NAME", activityName);
        startActivity(intent);
    }
}