package com.example.whatsappchat.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappchat.Models.MessageModel;
import com.example.whatsappchat.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends  RecyclerView.Adapter{
    ArrayList<MessageModel> messageModels;

    Context context;
    String recId;
    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;


    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String recId) {
        this.messageModels = messageModels;
        this.context = context;
        this.recId = recId;
    }

    public ChatAdapter(ArrayList<MessageModel> messageModels, View.OnClickListener onClickListener, String receiveId) {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==SENDER_VIEW_TYPE){
            View view= LayoutInflater.from(context).inflate(R.layout.sender_chat_sample,parent,false);
            return new SenderViewHolder(view);
        }
        else{
            View view= LayoutInflater.from(context).inflate(R.layout.receiver_chat_sample,parent,false);
            return new ReceiverViewHolder(view);

        }
    }
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel=messageModels.get(position);
        //alert dialog for deleting message
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("DELETE")
                        .setMessage("Delete this message?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                String sender=FirebaseAuth.getInstance().getUid()+recId;

                                database.getReference().child("chats").child(sender)
                                        .child(messageModel.getMessageId())
                                        .setValue(null);
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
                return false;
            }
        });
        if(holder.getClass()==SenderViewHolder.class) {
            //sending image in chat
            if(messageModel.getMessage().equals("photo")){
            ((SenderViewHolder)holder).imgchat.setVisibility(View.VISIBLE);
            ((SenderViewHolder)holder).senderMsg.setVisibility(View.GONE);
            Glide.with(context).load(messageModel.getImageurl())
                    .placeholder(R.drawable.imageplaceholder)
                    .into((ImageView) ((SenderViewHolder) holder).imgchat);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());
            ((SenderViewHolder)holder).senderTime.setText(currentDateandTime);
            }

           ((SenderViewHolder)holder).senderMsg.setText(messageModel.getMessage());
          //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            String currentDateandTime = sdf.format(new Date());
            ((SenderViewHolder)holder).senderTime.setText(currentDateandTime);

        }
        else{
            //receiving image in chat
            if(messageModel.getMessage().equals("photo")){
                ((ReceiverViewHolder)holder).imgrechat.setVisibility(View.VISIBLE);
                ((ReceiverViewHolder)holder).receiverMsg.setVisibility(View.GONE);
                Glide.with(context).load(messageModel.getImageurl())
                        .placeholder(R.drawable.imageplaceholder)
                        .into((ImageView) ((ReceiverViewHolder) holder).imgrechat);
            }

            ((ReceiverViewHolder)holder).receiverMsg.setText(messageModel.getMessage());
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return  SENDER_VIEW_TYPE;
        }
        else{
            return RECEIVER_VIEW_TYPE;
        }
        //return super.getItemViewType(position);
    }


    public int getItemCount() {
        return messageModels.size();
    }

    public  class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView receiverMsg,receiverTime;
        View imgrechat;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            imgrechat=itemView.findViewById(R.id.imagereceive);
            receiverMsg=itemView.findViewById(R.id.receiverText);
            receiverTime=itemView.findViewById(R.id.receiverTime);
        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderMsg,senderTime;
        View imgchat;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg=itemView.findViewById(R.id.senderText);
            senderTime=itemView.findViewById(R.id.senderTime);
            imgchat=itemView.findViewById(R.id.imagechat);
        }
    }

}
