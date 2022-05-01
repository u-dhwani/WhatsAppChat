package com.example.whatsappchat;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.whatsappchat.Adapter.FragmentsAdapter;
import com.example.whatsappchat.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        binding.viewPager.setAdapter((new FragmentsAdapter(getSupportFragmentManager())));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.women_safety:
                Intent intents=new Intent(MainActivity.this,SafetyActivity.class);
                startActivity(intents);
                break;
            case R.id.group_chat:
                Intent intent=new Intent(MainActivity.this,GroupChatActivity.class);
                startActivity(intent);
                break;
            case R.id.settings:
                Intent intent3=new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent3);
                break;
            case R.id.logout:
                auth.signOut();
                Intent intentt=new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intentt);
                break;
        }
        return true;
    }
    //online-typing-offline
    protected void onResume() {
        super.onResume();
        String currentId=FirebaseAuth.getInstance().getUid();
        database.getReference().child("attendance").child(currentId).setValue("Online");
    }

}