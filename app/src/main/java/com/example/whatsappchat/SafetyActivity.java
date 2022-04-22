package com.example.whatsappchat;

import static android.Manifest.permission.CALL_PHONE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationSettingsRequest;

import java.util.ArrayList;

public class SafetyActivity extends AppCompatActivity {
    Button addcontact,emergency;
    private FusedLocationProviderClient client;
    SafetyDatabaseHandler mydb;
    private final int  REQUEST_CHECK_CODE=8989;
    private LocationSettingsRequest.Builder builder;
    String x="",y="";
    private static final int REQUEST_LOCATION=1;
    LocationManager locationManager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety);
        addcontact = findViewById(R.id.safetywelcomeadd);
        emergency = findViewById(R.id.safetyemergency);
        mydb = new SafetyDatabaseHandler(this);
        final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.emergency_siren);
        //takes permission from system to access location
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGPS();
        } else {
            startTracking();
        }

        addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(getApplicationContext(),SafetyRegisterActivity.class);
                startActivity(in);
            }
        });
       emergency.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               mp.start();
               Toast.makeText(getApplicationContext(), "PANIC BUTTON STARTED", Toast.LENGTH_SHORT).show();
               loadData();

           }
       });
    }
    private void loadData(){
        ArrayList<String> list=new ArrayList<>();
        Cursor data=mydb.getListContents();
        if(data.getCount()==0){
            Toast.makeText(this, "No Content To Show", Toast.LENGTH_SHORT).show();
        }
        else{
            String msg="I NEED HELP AT THIS LOCATION (LATITUDE):"+ x +"(LONGITUDE): "+y;
            String number="";
            while(data.moveToNext()){
                list.add(data.getString(1));
                number=number+data.getString(1)+(data.isLast()?"":";");
                call();
            }
            if(!list.isEmpty()){
                sendSms(number,msg,true);
            }
        }
    }
    private void sendSms(String number,String msg,boolean b){
        Intent smsIntent=new Intent(Intent.ACTION_SENDTO);
        Uri.parse("smsto:"+number);
        smsIntent.putExtra("smsbody",msg);
        startActivity(smsIntent);
    }
    private void call(){
        Intent i=new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:1000"));
        if(ContextCompat.checkSelfPermission(getApplicationContext(),CALL_PHONE )== PackageManager.PERMISSION_GRANTED){
            startActivity(i);
        }
        else{
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{CALL_PHONE},1);
            }
        }
    }
    private void startTracking(){
        if(ActivityCompat.checkSelfPermission(SafetyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                SafetyActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }else{
            Location locationGPS=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(locationGPS!=null){
                double lat=locationGPS.getLatitude();
                double longi=locationGPS.getLongitude();
                x=String.valueOf(lat);
                y=String.valueOf(longi);
            }
        else{
                Toast.makeText(this,"Unable To Find Location",Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void onGPS(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }
}