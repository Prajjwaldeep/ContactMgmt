package com.sbdev.contactmgmt;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    ArrayList<ContactModel> arrayList;
    Context context;

    public ContactAdapter(ArrayList<ContactModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        ContactModel model=arrayList.get(holder.getAdapterPosition());

        holder.phone.setText(model.getPhone());
        holder.name.setText(model.getName());
        Glide.with(context)
                .load(arrayList.get(holder.getAdapterPosition()).getLink())
                .placeholder(R.drawable.sample)
                .into(holder.img);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StorageReference storageReference= FirebaseStorage.getInstance().getReference("Images");
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Contacts");

                String e= FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0,FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf('@'));
                databaseReference.child(e).child(model.getPhone()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Contact Removed!", Toast.LENGTH_SHORT).show();
                    }
                });

                storageReference.child(e).child(model.getPhone()).delete();

            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,CreateContact.class);
                intent.putExtra("Name",model.getName());
                intent.putExtra("Phone",model.getPhone());
                if(model.getLink()!=null && !model.getLink().isEmpty())
                {
                    intent.putExtra("Link",model.getLink());
                }
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img;
        TextView name, phone;
        ImageView delete,edit;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            img=itemView.findViewById(R.id.itemUserImg);
            name=itemView.findViewById(R.id.itemName);
            phone=itemView.findViewById(R.id.itemPhone);
            delete=itemView.findViewById(R.id.itemDelete);
            edit=itemView.findViewById(R.id.itemEdit);

        }
    }

}
