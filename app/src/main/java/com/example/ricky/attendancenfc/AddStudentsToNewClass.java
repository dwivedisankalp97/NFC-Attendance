package com.example.ricky.attendancenfc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class AddStudentsToNewClass extends AppCompatActivity {
    @Override
    protected void onStop() {
        super.onStop();
        writeToStudentJson(classNo,classStudentList);
    }

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayList<String> studentRegNo;
    private ArrayList<Student> students;
    private TextView studentName;
    private TextView studentTag;
    private String classNo;
    private String subjectName;
    private ArrayList<Student> classStudentList;
    private ArrayList<ClassID> classList;
    private RecyclerView recyclerView;
    private Button addStudent;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_students_to_new_class);
        Intent intent = getIntent();
        classNo = intent.getStringExtra("classNo");
        subjectName = intent.getStringExtra("subjectName");


        classList = new ArrayList<>();
        classStudentList = new ArrayList<>();
        students = new ArrayList<>();
        studentRegNo = new ArrayList<>();

        getClassListFromJson("classes");
        addNewClasses(subjectName,classNo);
        writeToClassJson();
        //File file = new File(getApplicationContext().getFilesDir(), classNo + ".json");
        //file.createNewFile();

        String fileName = classNo+".json";
        String content = "";

        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("FILE CREATED  ", classNo + ".json");

        getStudentListFromJson("students",students);//List of all the students registered

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        studentName = findViewById(R.id.studentNameTextView);
        studentTag = findViewById(R.id.studentTagTextView);
        addStudent = findViewById(R.id.addStudentToClassButton);
        recyclerView = findViewById(R.id.recyclerView);
        save = findViewById(R.id.saveButton);


        StudentListAdapter mAdapter = new StudentListAdapter(this,R.layout.drop_down_list_student_list_item,studentRegNo);
        mAdapter.notifyDataSetChanged();
        //if(mAdapter != null) Log.e("sdlkafj;lkdfsja;lkfjkladjf;ldkjsa;kljfd;lksajf;kjasdlf","lksjdf;lasdkjf;lkjas;fd");
        autoCompleteTextView.setAdapter(mAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = autoCompleteTextView.getAdapter().getItem(position).toString();
                Toast.makeText(getBaseContext(),"Selected "+ selected,Toast.LENGTH_SHORT).show();
                position = studentRegNo.indexOf(selected);
                studentName.setText(students.get(position).getName());
                studentTag.setText(students.get(position).getTag());
            }
        });


        final RecyclerViewArrayListAdapter recyclerViewArrayListAdapter = new RecyclerViewArrayListAdapter(classStudentList);
        recyclerView.setAdapter(recyclerViewArrayListAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewStudent(studentName.getText().toString(),autoCompleteTextView.getText().toString(),studentTag.getText().toString(),classStudentList);
                recyclerViewArrayListAdapter.notifyDataSetChanged();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToStudentJson(classNo,classStudentList);
                finish();
            }
        });



    }


    private void writeToStudentJson(String fileName, ArrayList<Student> students){
        try {
            String jsonString = JSONSerializerStudent.studentToJson(students);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getBaseContext().openFileOutput(fileName + ".json", MODE_PRIVATE));
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

    public class StudentListAdapter extends ArrayAdapter<String> {

        private Context mContext;
        private ArrayList<String> streg = new ArrayList<>();
        private ListFilter listFilter = new ListFilter();
        private ArrayList<String> dataListAllItems;

        public StudentListAdapter(Context context, int resource,ArrayList<String> studentRegNo) {
            super(context, resource,studentRegNo);
            streg = studentRegNo;
            mContext = context;
        }

        @Override
        public int getCount() {
            return streg.size();
        }

        @Override
        public String getItem(int position) {
            Log.d("CustomListAdapter",
                    streg.get(position));
            return streg.get(position);
        }

        @Override
        public View getView(int position,  View convertView,  ViewGroup parent) {
            View listItem = convertView;
            if(listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.drop_down_list_student_list_item,parent,false);
            String currentStudentName = getItem(position);
            TextView textView = listItem.findViewById(R.id.studentRegNoTextView);
            textView.setText(currentStudentName);
            return listItem;
        }

        @Override
        public Filter getFilter() {
            return listFilter;
        }

        public class ListFilter extends Filter {
            private Object lock = new Object();

            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                FilterResults results = new FilterResults();
                if (dataListAllItems == null) {
                    synchronized (lock) {
                        dataListAllItems = new ArrayList<String>(streg);
                    }
                }

                if (prefix == null || prefix.length() == 0) {
                    synchronized (lock) {
                        results.values = dataListAllItems;
                        results.count = dataListAllItems.size();
                    }
                } else {
                    final String searchStrLowerCase = prefix.toString().toLowerCase();

                    ArrayList<String> matchValues = new ArrayList<String>();

                    for (String dataItem : dataListAllItems) {
                        if (dataItem.toLowerCase().startsWith(searchStrLowerCase)) {
                            matchValues.add(dataItem);
                        }
                    }

                    results.values = matchValues;
                    results.count = matchValues.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    streg = (ArrayList<String>)results.values;
                } else {
                    streg = null;
                }
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

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

    private void getStudentListFromJson(String fileName,ArrayList<Student> students){
        try {
            Log.e("INSIDE TRY", "to");
            String buffer = readFromFile(fileName);
            JSONObject obj = new JSONObject(buffer);
            JSONArray arr = obj.getJSONArray("students");
            for (int i=0; i<arr.length(); i++) {
                JSONObject entry = arr.getJSONObject(i);
                String tag = entry.getString("tag");
                JSONObject stud =  entry.getJSONObject("student");
                String name = stud.getString("name");
                String id = stud.getString("regno");
                Log.e("STUFF ADDED", name + " " + id + " "+ tag);
                addNewStudent(name,id,tag,students);
                addNewStudent(id);
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void addNewStudent(String name, String id, String seriaNum,ArrayList<Student> students){
        Student newStudent = new Student(name,id,false,seriaNum);

        students.add(newStudent);
    }

    private void addNewStudent(String id){
        studentRegNo.add(id);
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
