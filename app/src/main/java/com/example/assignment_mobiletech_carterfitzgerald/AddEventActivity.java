package com.example.assignment_mobiletech_carterfitzgerald;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.GenericArrayType;

public class AddEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("ACTIVITY_NAME")) {
            String activityName = intent.getStringExtra("ACTIVITY_NAME");
            TextView textView = findViewById(R.id.textView3);
            textView.setText(activityName);
        }

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.location_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void openEditExistingActivity(View view) {
        TextView activityText = findViewById(R.id.textView3);
        EditText titleEditText = findViewById(R.id.details1);
        EditText summaryEditText = findViewById(R.id.details2);
        EditText timeEditText = findViewById(R.id.details3);
        EditText datesEditText = findViewById(R.id.details4);
        EditText contactEditText = findViewById(R.id.details5);
        Spinner spinner = findViewById(R.id.spinner);

        if (titleEditText.getText().toString().isEmpty() ||
                summaryEditText.getText().toString().isEmpty() ||
                timeEditText.getText().toString().isEmpty() ||
                datesEditText.getText().toString().isEmpty() ||
                contactEditText.getText().toString().isEmpty()) {
            return;
        }

        if (spinner.getSelectedItemPosition() == 0) {
            return;
        }

        Intent intent = new Intent(this, EditExistingActivity.class);
        intent.putExtra("activityName", activityText.getText().toString());
        intent.putExtra("title", titleEditText.getText().toString());
        intent.putExtra("summary", summaryEditText.getText().toString());
        intent.putExtra("times", timeEditText.getText().toString());
        intent.putExtra("dates", datesEditText.getText().toString());
        intent.putExtra("contact", contactEditText.getText().toString());
        intent.putExtra("spinner", spinner.getSelectedItem().toString());

        startActivity(intent);
    }
}