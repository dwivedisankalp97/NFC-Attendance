package com.example.ricky.attendancenfc;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class NewClass extends AppCompatActivity {

    EditText classNameEditText;
    EditText classNumberEditText;
    Button addClassButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_class);
        classNameEditText = findViewById(R.id.classNameEditText);
        classNumberEditText = findViewById(R.id.classNoEditText);
        addClassButton = findViewById(R.id.addClassButton);

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(classNameEditText.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(),"Enter a subject name",Toast.LENGTH_SHORT).show();
                }
                else if(classNumberEditText.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(),"Enter Class Number",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(getBaseContext(),AddStudentsToNewClass.class);
                    intent.putExtra("classNo",classNumberEditText.getText().toString());
                    intent.putExtra("subjectName",classNameEditText.getText().toString());
                    startActivity(intent);
                }
            }
        });

    }
}
