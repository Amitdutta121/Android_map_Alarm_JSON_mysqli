package com.example.lab4;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {

    public static final String dbname = "mydb.db";
    public static final int version = 1;
    public Context context;

    public MyHelper(Context context) {
        super(context, dbname, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {

        String sql = "CREATE TABLE employee (_id INTEGER PRIMARY KEY AUTOINCREMENT , NAME TEXT, LOCATION TEXT)";
        db.execSQL(sql);

//        String url = "https://anontech.info/courses/cse491/employees.json";
//
//        RequestQueue queue = Volley.newRequestQueue(context);
//
//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        Log.d("CODE", response);
//                        GsonBuilder gsonBuilder = new GsonBuilder();
//                        Gson gson = gsonBuilder.create();
//                        Employee[] employees = gson.fromJson(response, Employee[].class);
//
//                        for (int i=0; i< employees.length; i++){
//                            insertData(employees[i],db);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);


    }
    public void insertData(Employee employee, SQLiteDatabase database){
        ContentValues values = new ContentValues();
        values.put("NAME", employee.getName());
        values.put("LOCATION", employee.getLocation()+"");
        database.insert("employee", null, values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
