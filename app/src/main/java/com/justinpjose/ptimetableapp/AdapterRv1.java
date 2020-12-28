package com.justinpjose.ptimetableapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterRv1 extends RecyclerView.Adapter<AdapterRv1.ViewHolder> {

    private List<String> mTitles;
    private List<String> mDates;
    private List<String> mTimes;
    private List<String> mNotes;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    AdapterRv1(Context context, List<String> mTitles, List<String> mDates, List<String> mTimes, List<String> mNotes) {
        this.mInflater = LayoutInflater.from(context);
        this.mTitles = mTitles;
        this.mDates = mDates;
        this.mTimes = mTimes;
        this.mNotes = mNotes;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = mTitles.get(position);
        holder.mTvTitle.setText(title);
        String date = mDates.get(position);
        holder.myTvDate.setText("[ "+date+" ]");
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTvTitle;
        TextView myTvDate;

        ViewHolder(View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.title);
            myTvDate = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick1(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getTitle(int id) {
        return mTitles.get(id);
    }

    String getDate(int id) {
        return mDates.get(id);
    }

    String getTime(int id) {
        return mTimes.get(id);
    }

    String getNote(int id) {
        return mNotes.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick1(View view, int adapterPosition);
    }
}
