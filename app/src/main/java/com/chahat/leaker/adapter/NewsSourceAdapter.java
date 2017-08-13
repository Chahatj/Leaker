package com.chahat.leaker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.chahat.leaker.R;
import com.chahat.leaker.object.NewsSourceObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chahat on 9/8/17.
 */

public class NewsSourceAdapter extends RecyclerView.Adapter<NewsSourceAdapter.NewsSourceViewHolder> {

    private List<NewsSourceObject> sourceList;
    private final OnNewsSourceClick onNewsSourceClick;

    public NewsSourceAdapter(OnNewsSourceClick click){
        sourceList = new ArrayList<>();
        onNewsSourceClick = click;
    }

    public interface OnNewsSourceClick{
        void newSourceClickHandler(String channelName,String channelId);
    }

    @Override
    public NewsSourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.news_source_item,parent,false);
        return new NewsSourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsSourceViewHolder holder, int position) {

        NewsSourceObject newsSourceObject = sourceList.get(position);

        holder.textViewId.setText(newsSourceObject.getId());
        holder.textViewTitle.setText(newsSourceObject.getName());

    }

    public void setSourceList(List<NewsSourceObject> sourceList) {
        this.sourceList = sourceList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (sourceList.size()>0){
            return sourceList.size();
        }

        return 0;
    }

    public class NewsSourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView textViewTitle;
        private final TextView textViewId;

        public NewsSourceViewHolder(View itemView) {
            super(itemView);

            textViewId = (TextView) itemView.findViewById(R.id.news_source_id_textView);
            textViewTitle = (TextView) itemView.findViewById(R.id.news_source_title_textView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNewsSourceClick.newSourceClickHandler(sourceList.get(getAdapterPosition()).getName(),
                    sourceList.get(getAdapterPosition()).getId());
        }
    }
}
