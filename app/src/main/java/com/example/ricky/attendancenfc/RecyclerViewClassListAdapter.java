package com.example.ricky.attendancenfc;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewClassListAdapter extends RecyclerView.Adapter<RecyclerViewClassListAdapter.MyViewHolder> {

    public ArrayList<ClassID> mData;
    public ArrayList<ClassID> selectedClasses;

    public interface OnItemClickListener {
        void onItemClick(ClassID classID);
    }

    public interface OnItemLongClickListener{
        public boolean onItemLongClicked(int position);
    }
    private final OnItemClickListener listener;
    private final OnItemLongClickListener longListener;


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView classLocation;
        public TextView classSubject;
        public LinearLayout linearLayout;
        public MyViewHolder(View view){
            super(view);
            linearLayout = view.findViewById(R.id.recyclerViewClassLinearLayout);
            classSubject = view.findViewById(R.id.classSubject);
            classLocation = view.findViewById(R.id.classLocation);
        }

        public void bind(final ClassID classID, final int position, final OnItemClickListener listener, final OnItemLongClickListener longListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(classID);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longListener.onItemLongClicked(position);
                    return false;
                }
            });
        }

    }

    public RecyclerViewClassListAdapter(ArrayList<ClassID> data, OnItemClickListener listener, OnItemLongClickListener longListener)
    {
        this.listener = listener;
        this.longListener = longListener;
        mData = data;
        selectedClasses = new ArrayList<>();
    }

    public void changeBackgroundColor(ClassID classID){
        if(selectedClasses.contains(classID)){
            selectedClasses.remove(classID);
        }
        else{
            selectedClasses.add(classID);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerviewlistitem,viewGroup,false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ClassID c = mData.get(i);
        if(selectedClasses.contains(c))
            myViewHolder.linearLayout.setBackgroundColor(Color.WHITE);
        else myViewHolder.linearLayout.setBackgroundColor(Color.RED);
        myViewHolder.classSubject.setText(c.getSubjectName());
        myViewHolder.classLocation.setText(c.getClassNumber());
        myViewHolder.bind(mData.get(i),i,listener,longListener);
    }


    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


}
