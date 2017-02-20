package com.example.android.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{

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
}

