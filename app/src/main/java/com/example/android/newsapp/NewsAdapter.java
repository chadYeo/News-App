package com.example.android.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{

    private static final String LOG_TAG= NewsAdapter.class.getSimpleName();

    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;
    }

    private List<News> newsList;

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sectionName_textView) TextView mSectionName;
        @BindView(R.id.webTitle_textView) TextView mWebTitle;
        @BindView(R.id.type_value_textView) TextView mType;
        @BindView(R.id.webPublicationDate_value_textView) TextView mWebPublicationDate;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        NewsViewHolder newsViewHolder = new NewsViewHolder(itemView);
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        final News news = newsList.get(position);
        holder.mSectionName.setText(news.getSectionName());
        holder.mWebTitle.setText(news.getWebTitle());
        holder.mType.setText(news.getType());
        holder.mWebPublicationDate.setText(news.getWebPublicationDate());

        String strCurrentDate = holder.mWebPublicationDate.getText().toString();
        String changedDate = parseDateToddMMyyyy(strCurrentDate);
        holder.mWebPublicationDate.setText(changedDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri newsUri = Uri.parse(news.getWebUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                ((MainActivity)v.getContext()).startActivity(webIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void clear() {
        newsList.clear();
        notifyDataSetChanged();
    }

    public String parseDateToddMMyyyy(String time) {

        String[] dateArr = time.split("T");
        String date = dateArr[0];
        String hour = dateArr[1].substring(0, dateArr[1].length()-1);
        String updatedInputDate = date + " " + hour;

        String oldFormat= "yyyy-MM-dd HH:mm:ss";
        String newFormat= "MM/dd/yyyy h:mm a";

        String formatedDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat);
        Date myDate = null;
        try {
            myDate = dateFormat.parse(updatedInputDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
        formatedDate = timeFormat.format(myDate);

        return formatedDate;
    }
}

