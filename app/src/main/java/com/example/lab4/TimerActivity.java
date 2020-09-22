package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TimerActivity extends AppCompatActivity {

    EditText getTime;
    Button setTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        getTime = findViewById(R.id.getTime);
        setTime = findViewById(R.id.setTime);


        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting time in seconds
                int time = Integer.parseInt(getTime.getText().toString());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Timer ended",Toast.LENGTH_SHORT).show();
                    }
                }, time*1000);
            }
        });


    }
}
