package com.sbdev.contactmgmt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContactList extends AppCompatActivity {

    private ImageView logout,add;
    private TextView email;

    private FirebaseAuth auth;

    private RecyclerView recyclerView;

    private ArrayList<ContactModel> arrayList;
    private ContactAdapter adapter;

    private DatabaseReference reference;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_contact_list);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        email=findViewById(R.id.contactEmail);
        logout=findViewById(R.id.contactLogout);
        add=findViewById(R.id.contactAdd);

        recyclerView=findViewById(R.id.contactRecycler);
        arrayList=new ArrayList<>();
        adapter=new ContactAdapter(arrayList,ContactList.this);
        recyclerView.setAdapter(adapter);

        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        auth=FirebaseAuth.getInstance();
        String e=auth.getCurrentUser().getEmail().substring(0,auth.getCurrentUser().getEmail().indexOf("@"));
        email.setText(auth.getCurrentUser().getEmail());

        reference= FirebaseDatabase.getInstance().getReference("Contacts");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactList.this,CreateContact.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                finishAffinity();
                startActivity(new Intent(ContactList.this,Login.class));
            }
        });

        reference.child(e).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    ContactModel model=dataSnapshot.getValue(ContactModel.class);
                    arrayList.add(model);

                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}