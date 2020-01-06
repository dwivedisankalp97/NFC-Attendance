package com.example.ricky.attendancenfc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button attendanceButton = findViewById(R.id.attendanceButton);
        final Button newClass = findViewById(R.id.newClass);
        final Button addStudents = findViewById(R.id.addStudents);
        String fileName = "classes.json";
        String content = "";

        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(fileName, Context.MODE_APPEND);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fileName = "students.json";

        outputStream = null;
        try {
            outputStream = openFileOutput(fileName, Context.MODE_APPEND);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        attendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),ClassNavigation.class);
                startActivity(intent);
            }
        });

        addStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),newStudents.class);
                startActivity(intent);
            }
        });
        newClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),NewClass.class);
                startActivity(intent);
            }
        });

    }
}
