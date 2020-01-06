package com.example.ricky.attendancenfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class newStudents extends AppCompatActivity {

    EditText editTextName;
    EditText editTextRegNo;
    EditText editTextTag;
    Button saveButton;

    private HashMap<String, Student> students = new HashMap<>();

    private final String[][] techList = new String[][] {
            new String[] {
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_students);
        editTextTag = findViewById(R.id.editTextTag);
        editTextRegNo = findViewById(R.id.editTextRegNo);
        editTextName = findViewById(R.id.editTextName);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStudentListFromJson();
                addNewStudent(editTextName.getText().toString(),editTextRegNo.getText().toString(),editTextTag.getText().toString());
                writeToStudentJson();
                if(students.containsKey(editTextTag.getText().toString())) {
                    Toast.makeText(getBaseContext(), students.get(editTextTag.getText().toString()).getName(), Toast.LENGTH_LONG).show();
                    Log.i("data ", students.get(editTextTag.getText().toString()).getName());

                    for (Map.Entry<String,Student> entry : students.entrySet()) {
                        String key = entry.getKey();
                        Log.i("data ", key);
                        // do stuff
                    }
                }
                finish();
            }
        });


    }

    private void writeToStudentJson(){
        try {
            String jsonString = JSONSerializerStudent.studentToJson(students);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getBaseContext().openFileOutput("students.json", MODE_PRIVATE
            ));
            outputStreamWriter.write(jsonString);
            outputStreamWriter.close();
        }
        catch (FileNotFoundException f)
        {
            File file = new File(getApplicationContext().getFilesDir(),"students.json");
            Toast.makeText(getBaseContext(),"JSON file created",Toast.LENGTH_LONG).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

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

    private void getStudentListFromJson(){
        try {
            Log.e("INSIDE TRY", "to");
            String buffer = readFromFile("students");
            JSONObject obj = new JSONObject(buffer);
            JSONArray arr = obj.getJSONArray("students");
            for (int i=0; i<arr.length(); i++) {
                JSONObject entry = arr.getJSONObject(i);
                String tag = entry.getString("tag");
                JSONObject stud =  entry.getJSONObject("student");
                String name = stud.getString("name");
                String id = stud.getString("regno");
                Log.e("STUFF ADDED", name + " " + id + " "+ tag);
                addNewStudent(name,id,tag);
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void addNewStudent(String name, String id, String seriaNum){
        Student newStudent = new Student(name,id,false,seriaNum);

        students.put(seriaNum, newStudent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // creating pending intent:
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }


    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            String tag = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            editTextTag.setText(tag);
        }

    }
}
