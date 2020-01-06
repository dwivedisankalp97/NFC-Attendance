package com.example.ricky.attendancenfc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class JSONSerializerClass {

    public static String ClassToJson(ArrayList<ClassID> classList){
        String subjectName;
        String classNo;
        try{
            JSONObject obj = new JSONObject();
            JSONArray classes = new JSONArray();
            for(ClassID cl : classList)
            {
                subjectName = cl.getSubjectName();
                classNo = cl.getClassNumber();
                JSONObject classJsonObject = new JSONObject();
                classJsonObject.put("classNo",classNo);
                classJsonObject.put("subjectName",subjectName);
                classes.put(classJsonObject);
            }
            obj.put("classes",classes);

            return obj.toString();

        }catch(JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
