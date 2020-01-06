package com.example.ricky.attendancenfc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class ClassNavigation extends AppCompatActivity {

    private ArrayList<ClassID> classList;
    private boolean multiSelect = false;
    private ArrayList<ClassID> selectedClasses = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerViewClassListAdapter mAdapter;
    Integer selectCount;
    Integer itemCount;
    private Menu mMenu;
    ClassID mostrecentlySelectedClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_navigation);
        classList = new ArrayList<>();
        getClassListFromJson("classes");


        mAdapter = new RecyclerViewClassListAdapter(classList, new RecyclerViewClassListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ClassID classID) {
                if(multiSelect){
                    selectItem(classID);
                   // Toast.makeText(getBaseContext(),selectCount,Toast.LENGTH_SHORT ).show();
                    Log.i("Select Count", selectCount.toString());
                }
                else {
                    Intent intent = new Intent(getBaseContext(), TakeAttendance.class);
                    intent.putExtra("classNo", classID.getClassNumber());
                    startActivityForResult(intent,0);
                }
            }
        }, new RecyclerViewClassListAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(int position) {
                ClassNavigation.this.startSupportActionMode(mActionModeCallbacks);
                selectItem(classList.get(position));
                return true;
            }
        });


        recyclerView = findViewById(R.id.recyclerViewClass);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                getClassListFromJson("classes");
                mAdapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(),"Classes Refreshed",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ActionMode.Callback mActionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            selectCount = 0;
            mMenu = menu;
            itemCount = 2;
            mode.getMenuInflater().inflate(R.menu.contextual_menu_class_navigation,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }


        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.item_delete:
                    deleteClasses(selectedClasses,classList);
                    mode.finish();
                    return true;
                case R.id.item_edit:
                    if(selectCount!=1){Toast.makeText(getBaseContext(),"Only one class may be edited at a time",Toast.LENGTH_SHORT).show(); return true;}
                    Intent intent = new Intent(getBaseContext(),EditClass.class);
                    intent.putExtra("subjectName",mostrecentlySelectedClass.getSubjectName());
                    intent.putExtra("classNo",mostrecentlySelectedClass.getClassNumber());
                    startActivity(intent);
                    mode.finish();
                    return true;

            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedClasses.clear();
            mAdapter.notifyDataSetChanged();
            writeToClassJson();
        }
    };

    void selectItem(ClassID item) {
        if (multiSelect) {
            if (selectedClasses.contains(item)) {
                selectCount--;
                selectedClasses.remove(item);
                mAdapter.changeBackgroundColor(item);
            } else {
                mostrecentlySelectedClass = item;
                selectCount++;
                selectedClasses.add(item);
                mAdapter.changeBackgroundColor(item);
            }
        }
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

    private void deleteClasses(ArrayList<ClassID> selectedClasses,ArrayList<ClassID> classList){
        for(ClassID classID: selectedClasses)
        {
            File file = new File(getApplicationContext().getFilesDir(),classID.getClassNumber() + ".json");
            if(file.exists())
            {
                file.delete();
            }
            classList.remove(classID);

        }

        return;
    }

    private void getClassList(){
        ClassID id = new ClassID("Mobile Programming","AB2 - 208");
        ClassID id1 = new ClassID("Mobile Programming Lab","AB1 - 206");
        classList = new ArrayList<ClassID>();
        classList.add(id1);
        classList.add(id);
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
}
