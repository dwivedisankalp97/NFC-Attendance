package com.example.ricky.attendancenfc;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class RecyclerViewStudentListAdapter extends RecyclerView.Adapter<RecyclerViewStudentListAdapter.MyViewHolder> {

    private ArrayList mData;

    public interface OnItemClickListener {
        void onItemClick(Student student);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView regNoTextView;
        public CheckBox regNoCheckBox;
        public TextView nameTextView;
        public TableLayout studentTableLayout;
        public MyViewHolder(View view){
            super(view);
            studentTableLayout = view.findViewById(R.id.studentTableLayout);
            nameTextView = view.findViewById(R.id.nameTextView);
            regNoTextView = view.findViewById(R.id.regNoTextView);
            regNoCheckBox = view.findViewById(R.id.regNoCheckBox);
        }

    }

    public RecyclerViewStudentListAdapter(Map<String,Student> data)
    {
        mData = new ArrayList<>();
        mData.addAll(data.entrySet());
    }

    @NonNull
    @Override
    public RecyclerViewStudentListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerviewstudentlistitem,viewGroup,false);
        RecyclerViewStudentListAdapter.MyViewHolder vh = new RecyclerViewStudentListAdapter.MyViewHolder(v);
        return vh;
    }


    public Map.Entry<String, Student> getItem(int pos){
        return (Map.Entry) mData.get(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewStudentListAdapter.MyViewHolder myViewHolder, int i) {
        Map.Entry<String, Student> c = getItem(i);
        if(c.getValue().isStatus())
            myViewHolder.studentTableLayout.setBackgroundColor(Color.parseColor("green"));
        else
            myViewHolder.studentTableLayout.setBackgroundColor(Color.parseColor("red"));
        myViewHolder.nameTextView.setText(c.getValue().getName());
        myViewHolder.regNoTextView.setText(c.getValue().getRegNo());
        myViewHolder.regNoCheckBox.setChecked(c.getValue().isStatus());
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }


}
