package com.example.toheed.teacherapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toheed.teacherapp.Cache.SharedPrefManger;
import com.example.toheed.teacherapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    FloatingActionButton buttonSignin;
    TextView textViewRegisteruser;

    //authentication initialization
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignin = findViewById(R.id.buttonSignin);
        textViewRegisteruser = findViewById(R.id.textViewRegisteruser);

        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        textViewRegisteruser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        //moving towards firebase and getting its instance
        mAuth = FirebaseAuth.getInstance();
    }

    private void loginUser() {
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            String message;
            editTextEmail.setError("please enter valid email");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            String message;
            editTextPassword.setError("please enter correct password");
            return;
        }

        //builtin method of firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //finish current activity
                            finish();
                            SharedPrefManger.getmInstance(getApplicationContext()).setTeacherID(task.getResult().getUser().getUid());
                            SharedPrefManger.getmInstance(getApplicationContext()).setTeacherName(task.getResult().getUser().getDisplayName());
                            Intent intent = new Intent(LoginActivity.this, ModuleActivity.class);
                            startActivity(intent);
                        } else {
                            //getting error message
                            task.getException();
                            Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
