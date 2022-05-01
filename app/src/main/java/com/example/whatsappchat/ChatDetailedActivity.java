package com.example.whatsappchat;

import static android.Manifest.permission.CALL_PHONE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappchat.Adapter.ChatAdapter;
import com.example.whatsappchat.Models.MessageModel;
import com.example.whatsappchat.Models.Users;
import com.example.whatsappchat.databinding.ActivityChatDetailedBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatDetailedActivity extends AppCompatActivity {
    ActivityChatDetailedBinding binding;
    FirebaseStorage storage;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ProgressDialog dialog;
    String senderId, senderRoom, receiverRoom;
    String receiveId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Image From Storage");
        dialog.setCancelable(false);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        receiveId = getIntent().getStringExtra("userId");
        senderId = auth.getUid();
        senderRoom = senderId + receiveId;
        receiverRoom = receiveId + senderId;
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");
        binding.userName.setText(userName);


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatDetailedActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //online-typing-offline
        database.getReference().child("attendance").child(receiveId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.getValue(String.class);
                    if (!status.isEmpty()) {
                        binding.status.setText(status);
                        binding.status.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.gallerylink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                // video/*
                //for all types */*
                startActivityForResult(intent, 17);

            }
        });
        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messageModels, this, receiveId);
        binding.chatRecyclerView.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatDetailedActivity.this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);
        final Handler handler = new Handler();
        binding.imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent video=new Intent(getApplicationContext(), VideoActivity.class);
                startActivity(video);
            }
        });
        binding.typemessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                database.getReference().child("attendance").child(senderId).setValue("Typing...");
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(usernamestoppedtyping, 1000);
            }

            Runnable usernamestoppedtyping = new Runnable() {
                @Override
                public void run() {
                    database.getReference().child("attendance").child(senderId).setValue("Online");

                }
            };
        });
        Picasso.get().load(profilePic).placeholder(R.drawable.person).into(binding.profileImage);

        database.getReference("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            MessageModel model = snapshot1.getValue(MessageModel.class);
                            model.setMessageId(snapshot1.getKey());
                            messageModels.add(model);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = binding.typemessage.getText().toString();
                final MessageModel model = new MessageModel(senderId, message);

                model.setTimestamp(new Date().getTime());
                binding.typemessage.setText("");
                database.getReference().child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .push()
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ChatDetailedActivity.this, "Message Send", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                ChatDetailedActivity.super.onActivityResult(requestCode, resultCode, data);

                if (requestCode == 17) {
                    if (data != null) {
                        if (data.getData() != null) {
                            Uri selectedImage = data.getData();
                            Calendar calendar = Calendar.getInstance();
                            StorageReference reference = storage.getReference().child("chats").child(calendar.getTimeInMillis() + "");
                            dialog.show();
                            reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    dialog.dismiss();
                                    if (task.isSuccessful()) {
                                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String filePath = uri.toString();
                                                String message = binding.typemessage.getText().toString();
                                                Date date = new Date();
                                                final MessageModel model = new MessageModel(senderId, message, date.getTime());
                                                model.setImageurl(filePath);
                                                model.setMessage("photo");
                                                model.setTimestamp(new Date().getTime());
                                                binding.typemessage.setText("");
                                                database.getReference().child("chats")
                                                        .child(senderRoom)
                                                        .push()
                                                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        database.getReference().child("chats")
                                                                .child(receiverRoom)
                                                                .push()
                                                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(ChatDetailedActivity.this, "Message Send", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }



protected void onResume(){
        super.onResume();
        String currentId=FirebaseAuth.getInstance().getUid();
        database.getReference().child("attendance").child(currentId).setValue("Online");
        }

protected void onPause(){
        super.onPause();
        String currentId=FirebaseAuth.getInstance().getUid();
        database.getReference().child("attendance").child(currentId).setValue("Offline");
        }
        }
