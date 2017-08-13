package com.chahat.leaker.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chahat.leaker.R;
import com.chahat.leaker.object.NewsObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chahat on 10/8/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsObject> mList;
    private final Context context;
    private final NewsItemClickListner newsItemClickListner;

    public NewsAdapter(Context context,NewsItemClickListner clickListner){
        mList = new ArrayList<>();
        this.context = context;
        newsItemClickListner = clickListner;
    }

    public interface NewsItemClickListner{
        void onNewsItemClick(NewsObject newsObject);
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.news_item,parent,false);
        return new NewsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {

        NewsObject newsObject = mList.get(position);
        holder.newsTitleTextView.setText(newsObject.getTitle());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Log.d("inNewsAdapter",sharedPreferences.getBoolean(context.getString(R.string.pref_check_image),true)+"");
        boolean value = sharedPreferences.getBoolean(context.getString(R.string.pref_check_image),true);
        if (value){
            Picasso.with(context).load(newsObject.getUrlImage()).into(holder.newsImageView);
        }else {
            holder.newsImageView.setImageResource(android.R.color.transparent);
        }

    }

    public void setmList(List<NewsObject> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mList.size()>0) return mList.size();
        return 0;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ImageView newsImageView;
        private final TextView newsTitleTextView;

        public NewsViewHolder(View itemView) {
            super(itemView);

            newsImageView = (ImageView) itemView.findViewById(R.id.news_image_view);
            newsTitleTextView = (TextView) itemView.findViewById(R.id.new_title_textView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            newsItemClickListner.onNewsItemClick(mList.get(getAdapterPosition()));
        }
    }
}
