package com.example.whatsappchat;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SafetyRegisterActivity extends AppCompatActivity {
    Button addphone,deletephone,viewphone;
    EditText phone;
    ListView listView;
    SQLiteOpenHelper s1;
    SQLiteDatabase sqLiteDb;
    SafetyDatabaseHandler mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        phone=findViewById(R.id.safetyphone);
        addphone=findViewById(R.id.safetyadd);
        deletephone=findViewById(R.id.safetydelete);
        viewphone=findViewById(R.id.safetyview);
        listView=findViewById(R.id.listphone);
        mydb=new SafetyDatabaseHandler(this);
        addphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sr=phone.getText().toString();
                adddata(sr);
                Toast.makeText(SafetyRegisterActivity.this,"Data Added Successfully",Toast.LENGTH_SHORT).show();
                phone.setText("");
            }
        });
        deletephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqLiteDb=mydb.getWritableDatabase();
                String x=phone.getText().toString();
                deletedata(x);
                Toast.makeText(SafetyRegisterActivity.this,"Data Deleted Successfully",Toast.LENGTH_SHORT).show();

            }
        });
        viewphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loaddata();
            }
        });
    }
    private void loaddata(){
        ArrayList<String> theList=new ArrayList<>();
        Cursor data=mydb.getListContents();
        if(data.getCount()==0){
            Toast.makeText(SafetyRegisterActivity.this,"There is no content to display",Toast.LENGTH_SHORT).show();
        }
        else{
            while(data.moveToNext()){
                theList.add(data.getString(1));
                ListAdapter listAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,theList);
                listView.setAdapter(listAdapter);
            }
        }
    }
    private void adddata(String newentry){
        boolean insertdata= mydb.addData(newentry);
        if(insertdata==true){
            Toast.makeText(SafetyRegisterActivity.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(SafetyRegisterActivity.this,"Unsuccessful",Toast.LENGTH_SHORT).show();
        }


    }
    private boolean deletedata(String x) {
        return sqLiteDb.delete(SafetyDatabaseHandler.TABLE_NAME ,SafetyDatabaseHandler.COL2 + "=?" , new String[]{x})>0;

    }
}