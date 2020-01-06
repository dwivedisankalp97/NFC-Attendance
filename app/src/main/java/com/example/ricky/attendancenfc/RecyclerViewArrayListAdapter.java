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

public class RecyclerViewArrayListAdapter extends RecyclerView.Adapter<RecyclerViewArrayListAdapter.MyViewHolder> {

    private ArrayList<Student> mData;

    public interface OnItemClickListener {
        void onItemClick(Student student);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView regNoTextView;
        public TextView nameTextView;
        public TableLayout studentTableLayout;
        public MyViewHolder(View view){
            super(view);
            studentTableLayout = view.findViewById(R.id.studentTableLayout);
            nameTextView = view.findViewById(R.id.nameTextView);
            regNoTextView = view.findViewById(R.id.regNoTextView);
        }

    }

    public RecyclerViewArrayListAdapter(ArrayList<Student> data)
    {
        mData = data;
    }

    @NonNull
    @Override
    public RecyclerViewArrayListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerviewarraylistitem,viewGroup,false);
        RecyclerViewArrayListAdapter.MyViewHolder vh = new RecyclerViewArrayListAdapter.MyViewHolder(v);
        return vh;
    }


    public Student getItem(int pos){
        return  mData.get(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewArrayListAdapter.MyViewHolder myViewHolder, int i) {
        Student c = getItem(i);
        myViewHolder.nameTextView.setText(c.getName());
        myViewHolder.regNoTextView.setText(c.getRegNo());
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }


}
