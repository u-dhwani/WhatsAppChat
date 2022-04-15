package com.example.whatsappchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatsappchat.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends AppCompatActivity {
    private EditText ic1,ic2,ic3,ic4,ic5,ic6;
    FirebaseAuth auth;
    FirebaseDatabase database;
    private String verificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        setContentView(R.layout.activity_verify_otpactivity);
        TextView textMobile=findViewById(R.id.textMobile);
        textMobile.setText(String.format(
                "91-%s",getIntent().getStringExtra("mobile")
        ));
        ic1=findViewById(R.id.inputCode1);
        ic2=findViewById(R.id.inputCode2);
        ic3=findViewById(R.id.inputCode3);
        ic4=findViewById(R.id.inputCode4);
        ic5=findViewById(R.id.inputCode5);
        ic6=findViewById(R.id.inputCode6);
        setupOTPInputs();
        final ProgressBar progressBar=findViewById(R.id.progressbar);
        final Button verifyotp=findViewById(R.id.VerifyOTP);
        verificationId=getIntent().getStringExtra("verificationId");
        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ic1.getText().toString().trim().isEmpty() || ic2.getText().toString().trim().isEmpty() ||
                        ic3.getText().toString().trim().isEmpty() ||
                        ic4.getText().toString().trim().isEmpty() ||
                        ic5.getText().toString().trim().isEmpty() ||
                        ic6.getText().toString().trim().isEmpty()){
                    Toast.makeText(VerifyOTPActivity.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code=ic1.getText().toString()+ic2.getText().toString()+ic3.getText().toString()+
                        ic4.getText().toString()+ic5.getText().toString()+ic6.getText().toString();
                if(verificationId!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    verifyotp.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(
                            verificationId,
                            code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    verifyotp.setVisibility(View.VISIBLE);
                                    if(task.isSuccessful()){
                                        FirebaseUser user=auth.getCurrentUser();
                                        Users users=new Users();
                                        users.setUserId(user.getUid());
                                        users.setUserName(user.getDisplayName());
                                       // users.setProfilepic(user.getPhotoUrl().toString());
                                        database.getReference().child("Users").child(user.getUid()).setValue(users);

                                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(VerifyOTPActivity.this, "Verification Code Entered Is Invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        findViewById(R.id.ResendOTP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        //Country code+mobile number
                        "+91"+getIntent().getStringExtra("mobile"),
                        //this means if otp is sent user can not get OTP for 60 seconds
                        60,
                        TimeUnit.SECONDS,
                        VerifyOTPActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newverificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                verificationId=newverificationId;
                                Toast.makeText(VerifyOTPActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

            }
        });
    }
    private void setupOTPInputs(){
        ic1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    ic2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ic2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    ic3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ic3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    ic4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ic4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    ic5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ic5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    ic6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}