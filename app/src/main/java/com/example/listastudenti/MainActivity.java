package com.example.listastudenti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;

    Button add_data;
    EditText add_name;


    ListView userlist;
    ArrayList<String> listItems;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db=new DatabaseHelper(this);

        listItems =new ArrayList<>();

        add_data=findViewById(R.id.add_data);
        add_name=findViewById(R.id.add_name);
        userlist=findViewById(R.id.users_list);
        
        viewData();


       /* add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=add_name.getText().toString();
                if( db.insertData(name)){
                    Toast.makeText(MainActivity.this, "data added", Toast.LENGTH_SHORT).show();
                    add_name.setText("");
                }else {
                    Toast.makeText(MainActivity.this, "data not added", Toast.LENGTH_SHORT).show();

                }
            }
        });*/
    }

    private void viewData() {
        Cursor cursor=db.viewData();

        if(cursor.getCount()==0){
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();

        }else
        {
            while(cursor.moveToNext()){
                listItems.add(cursor.getString(1));
            }

            adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
            userlist.setAdapter(adapter);


        }
    }

    public void onClickAdd(View view){
        String name=add_name.getText().toString();
        if( !name.equals("") && db.insertData(name)){
            Toast.makeText(MainActivity.this, "data added", Toast.LENGTH_SHORT).show();
            add_name.setText("");
        }else {
            Toast.makeText(MainActivity.this, "data not added", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        MenuItem searchItem=menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<String> userslist = new ArrayList<>();

                for(String user : listItems){
                    if(user.toLowerCase().contains(s.toLowerCase())){
                        userslist.add(user);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,userslist);
                userlist.setAdapter(adapter);


                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}