package com.sbdev.contactmgmt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Login extends AppCompatActivity {

    private EditText email, pass;
    private AppCompatButton login;
    private TextView reg;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_login);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        email=findViewById(R.id.loginEmailET);
        pass=findViewById(R.id.loginPassET);
        login=findViewById(R.id.btnLogin);
        reg=findViewById(R.id.loginUser);

        auth=FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null)
        {
            startActivity(new Intent(Login.this,ContactList.class));
            finishAffinity();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String e=email.getText().toString();
                String p=pass.getText().toString();

                if(e.isEmpty() || p.isEmpty())
                {
                    Toast.makeText(Login.this, "Field cannot be empty!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loginUser(e,p);
                }

            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
                finishAffinity();
            }
        });

    }

    private void loginUser(String e, String p)
    {

        auth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this,ContactList.class));
                    finishAffinity();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}