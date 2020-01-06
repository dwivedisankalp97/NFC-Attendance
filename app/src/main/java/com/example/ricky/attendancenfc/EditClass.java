package com.example.ricky.attendancenfc;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static com.example.ricky.attendancenfc.ClassID.search;

public class EditClass extends AppCompatActivity {
    EditText classNameEditText;
    EditText classNumberEditText;
    Button saveClassButton;
    Button cancelClassButton;
    private String originalSubjectName;
    private String originalClassNo;
    private String newSubjectName;
    private String newClassNo;
    private ArrayList<ClassID> classList = new ArrayList<>();
    private Handler h;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);
        Intent intent = getIntent();
        originalSubjectName = intent.getStringExtra("subjectName");
        originalClassNo = intent.getStringExtra("classNo");

        classNameEditText = findViewById(R.id.classNameEditText);
        classNumberEditText = findViewById(R.id.classNoEditText);
        saveClassButton = findViewById(R.id.saveClassButton);
        cancelClassButton = findViewById(R.id.cancelClassButton);

        classNameEditText.setText(originalSubjectName);
        classNumberEditText.setText(originalClassNo);

        h = new Handler() {
            public void handleMessage(Message msg){
                if(msg.what == 0){
                    Toast.makeText(getBaseContext(), "Class Number Already Exists", Toast.LENGTH_SHORT).show();
                }
            }
        };

        saveClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newClassNo = classNumberEditText.getText().toString();
                newSubjectName = classNameEditText.getText().toString();
                if(classNameEditText.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(),"Enter a subject name",Toast.LENGTH_SHORT).show();
                }
                else if(classNumberEditText.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(),"Enter Class Number",Toast.LENGTH_SHORT).show();
                }
                else if(newClassNo.equals(originalClassNo) && newSubjectName.equals(originalSubjectName))
                {
                    Toast.makeText(getBaseContext(),"No Information Modified",Toast.LENGTH_SHORT).show();
                }
                else{
                    startTask();
                    Toast.makeText(getBaseContext(),"Done",Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK,returnIntent);
                    finish();
                }
            }
        });

        cancelClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void startTask(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getClassListFromJson("classes");
                ClassID classID;
                File file = new File(getApplicationContext().getFilesDir(),originalClassNo + ".json");
                File newFile = new File(getApplicationContext().getFilesDir(), newClassNo + ".json");
                if(newFile.exists()){
                   // Toast.makeText(getBaseContext(),"Class Number already exists",Toast.LENGTH_SHORT).show();
                    h.sendEmptyMessage(0);
                }
                else{
                    file.renameTo(newFile);
                    classID = ClassID.search(classList,originalClassNo);
                    if(classID.getClassNumber().equals("")){
                        Log.e("Contains", " NO");
                    }
                    else {
                        classList.remove(classID);
                        ClassID newClassID = new ClassID(newSubjectName, newClassNo);
                        classList.add(newClassID);
                    }
                }
                writeToClassJson();
            }
        };
        new Thread(runnable).start();
    }

    private void writeToClassJson(){
        try {
            String jsonString = JSONSerializerClass.ClassToJson(classList);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getBaseContext().openFileOutput("classes.json", MODE_PRIVATE));
            outputStreamWriter.write(jsonString);
            outputStreamWriter.close();
        }
        catch (FileNotFoundException f)
        {
            File file = new File(getApplicationContext().getFilesDir(),"classes.json");
            Toast.makeText(getBaseContext(),"JSON file created",Toast.LENGTH_LONG).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private void getClassListFromJson(String fileName){
        try {
            Log.e("INSIDE TRY", "to");
            String buffer = readFromFile(fileName);
            JSONObject obj = new JSONObject(buffer);
            JSONArray arr = obj.getJSONArray("classes");
            for (int i=0; i<arr.length(); i++) {
                JSONObject entry = arr.getJSONObject(i);
                String classNo = entry.getString("classNo");
                String subjectName =  entry.getString("subjectName");
                Log.e("STUFF ADDED", subjectName + " " + classNo);
                addNewClasses(subjectName,classNo);
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void addNewClasses(String subjectName, String classNo){
        ClassID classID = new ClassID(subjectName,classNo);
        classList.add(classID);
    }

    private String readFromFile(String string) {

        String ret = "";
        InputStream inputStream = null;
        try {
            inputStream = openFileInput(string + ".json");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            File file = new File(getApplicationContext().getFilesDir(),string + ".json");
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }


}
