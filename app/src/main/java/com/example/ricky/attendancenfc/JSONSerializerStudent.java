package com.example.ricky.attendancenfc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class JSONSerializerStudent {


    public static String studentToJson(Map<String,Student> studentMap){
        String tag;
        Student student;
        try{
            JSONObject obj = new JSONObject();
            JSONArray students = new JSONArray();
            for(Map.Entry<String,Student> st : studentMap.entrySet())
            {
                tag = st.getKey();
                student = st.getValue();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("tag",tag);
                JSONObject studentJsonObject = new JSONObject();
                studentJsonObject.put("name",student.getName());
                studentJsonObject.put("regno",student.getRegNo());
                jsonObject.put("student",studentJsonObject);
                students.put(jsonObject);

            }
            obj.put("students",students);

            return obj.toString();

        }catch(JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String studentToJson(ArrayList<Student> studentArrayList){
        String tag;
        Student student;
        try{
            JSONObject obj = new JSONObject();
            JSONArray students = new JSONArray();
            for(Student st : studentArrayList)
            {
                tag = st.getTag();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("tag",tag);
                JSONObject studentJsonObject = new JSONObject();
                studentJsonObject.put("name",st.getName());
                studentJsonObject.put("regno",st.getRegNo());
                jsonObject.put("student",studentJsonObject);
                students.put(jsonObject);

            }
            obj.put("students",students);

            return obj.toString();

        }catch(JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
