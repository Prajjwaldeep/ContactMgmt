package com.sbdev.contactmgmt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Register extends AppCompatActivity {

    private EditText email, pass;
    private AppCompatButton register;
    private TextView log;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_register);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        email=findViewById(R.id.registerEmailET);
        pass=findViewById(R.id.registerPassET);
        register=findViewById(R.id.btnRegister);
        log=findViewById(R.id.registerUser);

        auth=FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null)
        {
            startActivity(new Intent(Register.this,ContactList.class));
            finishAffinity();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String e=email.getText().toString();
                String p=pass.getText().toString();

                if(e.isEmpty() || p.isEmpty())
                {
                    Toast.makeText(Register.this, "Field cannot be empty!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    registerUser(e,p);
                }

            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
                finishAffinity();
            }
        });

    }

    public void registerUser(String email, String pass)
    {

        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    Toast.makeText(Register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register.this,ContactList.class));
                    finishAffinity();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}