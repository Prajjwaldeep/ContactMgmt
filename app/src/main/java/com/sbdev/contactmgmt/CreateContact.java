package com.sbdev.contactmgmt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateContact extends AppCompatActivity {

    private CircleImageView imageView;
    private EditText name,phone;
    private AppCompatButton submit;
    private static final int reqCode=1;

    private Uri imageURI=null;

    private FirebaseAuth auth;

    private DatabaseReference databaseReference;

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_create_contact);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        imageView=findViewById(R.id.createImg);
        name=findViewById(R.id.createNameET);
        phone=findViewById(R.id.createPhoneET);
        submit=findViewById(R.id.createSubmit);

        auth=FirebaseAuth.getInstance();
        String s=auth.getCurrentUser().getEmail().substring(0,auth.getCurrentUser().getEmail().indexOf('@'));
        Log.d("Email",s);

        storageReference= FirebaseStorage.getInstance().getReference("Images");
        databaseReference= FirebaseDatabase.getInstance().getReference("Contacts");

        if(getIntent().getStringExtra("Name")!=null)
        {
            name.setText(getIntent().getStringExtra("Name"));
        }
        if(getIntent().getStringExtra("Phone")!=null)
        {
            phone.setText(getIntent().getStringExtra("Phone"));
        }
        if(getIntent().getStringExtra("Link")!=null)
        {
            Glide.with(CreateContact.this)
                    .load(getIntent().getStringExtra("Link"))
                    .placeholder(R.drawable.sample)
                    .into(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String n=name.getText().toString();
                String p=phone.getText().toString();

                if(n.isEmpty() || p.isEmpty())
                {
                    Toast.makeText(CreateContact.this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    pushDataToFirebase(n,p,s);
                }

            }
        });

    }

    private void pushDataToFirebase(String n, String p, String s)
    {

        HashMap<String,String> map=new HashMap<>();
        map.put("name",n);
        map.put("phone",p);

        if(imageURI!=null)
        {
            storageReference.child(s).child(p).putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    storageReference.child(s).child(p).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            map.put("link",uri.toString());
                            databaseReference.child(s).child(p).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(CreateContact.this, "Data added!", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            });
                        }
                    });

                }
            });
        }
        else
        {
            databaseReference.child(s).child(p).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(CreateContact.this, "Data added!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            });
        }


    }

    private void openGallery()
    {

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,reqCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==reqCode && resultCode==RESULT_OK && data.getData()!=null)
        {
            imageURI=data.getData();
            imageView.setImageURI(imageURI);
        }

    }
}