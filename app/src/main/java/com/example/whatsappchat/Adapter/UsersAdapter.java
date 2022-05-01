package com.example.whatsappchat.Adapter;
import static android.Manifest.permission.CALL_PHONE;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappchat.ChatDetailedActivity;
import com.example.whatsappchat.Models.Users;
import com.example.whatsappchat.R;
import com.example.whatsappchat.VideoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{
    ArrayList<Users> list;
    Context context;

    public UsersAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.show_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users=list.get(position);
        Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.person).into(holder.image);
        holder.Name.setText(users.getUserName());
        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(FirebaseAuth.getInstance().getUid()+users.getUserId())
                .orderByChild("timestamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            for(DataSnapshot snapshot1:snapshot.getChildren()){
                                holder.lastMessage.setText(snapshot1.child("message").getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ChatDetailedActivity.class);
                intent.putExtra("userId",users.getUserId());
                intent.putExtra("profilePic",users.getProfilepic());
                intent.putExtra("userName",users.getUserName());
                context.startActivity(intent);
            }
        });


       holder.call.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i=new Intent(Intent.ACTION_CALL);
               i.setData(Uri.parse("tel:"+users.getPhone()));
               if(ContextCompat.checkSelfPermission(context.getApplicationContext(),CALL_PHONE )== PackageManager.PERMISSION_GRANTED){
                   context.startActivity(i);
               }
               else{
                   if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                       requestPermissions(new String[]{CALL_PHONE},1);
                   }
               }
           }

           private void requestPermissions(String[] strings, int i) {
           }

       });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView call,videocall;
        ImageView image;
        TextView Name,lastMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            call=itemView.findViewById(R.id.callaudiochat);
            image=itemView.findViewById(R.id.profile_image);
            Name=itemView.findViewById(R.id.chats_name);
            lastMessage=itemView.findViewById(R.id.chats_lastmessage);

        }


    }
}
