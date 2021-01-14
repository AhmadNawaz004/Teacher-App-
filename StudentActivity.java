package com.example.toheed.teacherapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.toheed.teacherapp.Cache.SharedPrefManger;
import com.example.toheed.teacherapp.Model.Student;
import com.example.toheed.teacherapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentActivity extends AppCompatActivity {
    EditText editTextName, editTextRegistrationNumber;
    Spinner spinnerSemester;
    Button buttonSubmit;

    DatabaseReference mDatabaseRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        editTextName = findViewById(R.id.editTextName);
        spinnerSemester = findViewById(R.id.spinnerSemester);
        editTextRegistrationNumber = findViewById(R.id.editTextRegistrationNumber);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        mAuth = FirebaseAuth.getInstance();

        //getting current teacher id
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("student").child(user.getUid()).child(SharedPrefManger.getmInstance(getApplicationContext()).getSubjectId());

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
            }
        });
    }

    private void addStudent() {
        final String name = editTextName.getText().toString().trim();
        final String semester = spinnerSemester.getSelectedItem().toString().trim();
        final String registrationnumber = editTextRegistrationNumber.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            String message;
            editTextName.setError("please enter student name");
            return;
        }
        if (TextUtils.isEmpty(registrationnumber)) {
            String message;
            editTextRegistrationNumber.setError("please enter registration number");
            return;
        }

        //getting random key
        String id = mDatabaseRef.push().getKey();
        Student student = new Student(id, name, semester, registrationnumber);
        mDatabaseRef.child(id).setValue(student);


        Toast.makeText(getApplicationContext(), "student account created", Toast.LENGTH_SHORT).show();
        editTextName.setText("");
        editTextRegistrationNumber.setText("");


    }
}
	 