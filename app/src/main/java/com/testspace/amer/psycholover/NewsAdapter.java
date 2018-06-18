package com.testspace.amer.psycholover;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private ArrayList<News> news = new ArrayList<>();
    private OnItemSelectedListener listener;

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.bind(news.get(position));
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public void setNews(ArrayList<News> news) {
        this.news = news;
    }

    public void setListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    private OnItemSelectedListener getListener() {
        return listener;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.newsItemImgImageView)
        ImageView newsItemImgImageView;
        @BindView(R.id.newsItemTitleTextView)
        TextView newsItemTitleTextView;
        @BindView(R.id.newsItemDescriptionTextView)
        TextView newsItemDescriptionTextView;
        @BindView(R.id.newsItemSectionValueTextView)
        TextView newsItemSectionValueTextView;
        @BindView(R.id.newsItemSectionTitleTextView)
        TextView newsItemSectionTitleTextView;
        @BindView(R.id.newsItemAuthorValueTextView)
        TextView newsItemAuthorValueTextView;
        @BindView(R.id.newsItemAuthorTitleTextView)
        TextView newsItemAuthorTitleTextView;
        @BindView(R.id.newsItemDateTitleTextView)
        TextView newsItemDateTitleTextView;
        @BindView(R.id.newsItemDateValueTextView)
        TextView newsItemDateValueTextView;
        View itemView;

        NewsViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        void bind(final News news) {
            if (news.getImgBitmap() == News.NO_IMGBITMAP || news.getImgBitmap() == null) {
                newsItemImgImageView.setImageResource(R.drawable.love_mind);
            } else {
                newsItemImgImageView.setImageBitmap(news.getImgBitmap());
            }
            newsItemTitleTextView.setText(news.getTitle());
            if (!news.getDescription().equals(News.NO_DESCRIPTION)) {
                newsItemDescriptionTextView.setText(news.getDescription());
            } else {
                newsItemDescriptionTextView.setVisibility(View.INVISIBLE);
            }
            if (!news.getSection().equals(News.NO_SECTION)) {
                newsItemSectionValueTextView.setText(news.getSection());
            } else {
                newsItemSectionValueTextView.setVisibility(View.INVISIBLE);
                newsItemSectionTitleTextView.setVisibility(View.INVISIBLE);
            }
            if (!news.getAuthor().equals(News.NO_AUTHOR)) {
                newsItemAuthorValueTextView.setText(news.getAuthor());
            } else {
                newsItemAuthorValueTextView.setVisibility(View.INVISIBLE);
                newsItemAuthorTitleTextView.setVisibility(View.INVISIBLE);
            }
            if (!news.getDate().equals(News.NO_DATE)) {
                newsItemDateValueTextView.setText(news.getDate());
            } else {
                newsItemDateValueTextView.setVisibility(View.INVISIBLE);
                newsItemDateTitleTextView.setVisibility(View.INVISIBLE);
            }
            newsItemDescriptionTextView.setText(news.getDescription());
            newsItemDateValueTextView.setText(news.getDate());
            newsItemTitleTextView.setText(news.getTitle());
            newsItemSectionValueTextView.setText(news.getSection());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getListener() != null) {
                        getListener().onItemClick(news);
                    }
                }
            });
        }
    }

    interface OnItemSelectedListener {
        void onItemClick(News news);
    }
}