package com.example.android.newsapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{

    private List<News> newsList;

    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;
    }

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
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.mSectionName.setText(news.getSectionName());
        holder.mWebTitle.setText(news.getWebTitle());
        holder.mType.setText(news.getType());
        holder.mWebPublicationDate.setText(news.getWebPublicationDate());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

}

