package com.example.lab4;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView empListview;
    String finalresponse = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        empListview = findViewById(R.id.employees);

        final ArrayList<String> dataNames = new ArrayList<String>();

        final MyHelper helper = new MyHelper(this);
        final SQLiteDatabase databaseRead = helper.getReadableDatabase();
        final SQLiteDatabase databaseWrite = helper.getWritableDatabase();

        String url = "https://anontech.info/courses/cse491/employees.json";

        final SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        final boolean firststart = preferences.getBoolean("firststart", true);


        RequestQueue queue = Volley.newRequestQueue(this);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("CODE", response);
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        final Employee[] employees = gson.fromJson(response, Employee[].class);


                        Log.d("CODE", employees[4].getLocation()+"");
                        //adding data to database

                        if (firststart) {
                            for (int i = 0; i < employees.length; i++) {
                                helper.insertData(employees[i], databaseWrite);
                            }
                            SharedPreferences preferences1 = getSharedPreferences("prefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences1.edit();
                            editor.putBoolean("firststart", false);
                            editor.apply();
                        }

                        Cursor cursor = databaseRead.rawQuery("SELECT NAME FROM employee", new String[]{});

                        if (cursor != null){
                            cursor.moveToFirst();
                        }

                        do {
                            String name = cursor.getString(0);
                            dataNames.add(name);
                        }
                        while (cursor.moveToNext());

                        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, dataNames.toArray());
                        empListview.setAdapter(adapter);

                        empListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                final int pos = position;
                                Log.d("CODE",position+"");
                                if (employees[position].getLocation() == null){
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                    alertDialog.setMessage("No location found do you want to set the current location ?").setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                                                    intent.putExtra("data", "{latitude=-60.489023, longitude=-32.311668},SSSS,"+pos);
                                                    employees[position].setLocation("{latitude=23.797340, longitude=90.422277}");
                                                    startActivity(intent);
//                                                    Toast.makeText(getApplicationContext(),"Current location saved", Toast.LENGTH_SHORT).show();

                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alert = alertDialog.create();
                                    alert.setTitle("Dialog Header");
                                    alert.show();
                                }else {

                                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                                    intent.putExtra("data", employees[position].getLocation() + ",noSave,"+pos);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);



    }
}
