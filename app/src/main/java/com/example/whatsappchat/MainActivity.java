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

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
}