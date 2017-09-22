package com.cnswan.juggle.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cnswan.juggle.R;
import com.cnswan.juggle.bean.movie.PersonBean;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailAdapter extends RecyclerView.Adapter<MovieDetailAdapter.MovieDetailHolder> {
    List<PersonBean> list = new ArrayList<>();

    public void addAll(List<PersonBean> list) {
        this.list.addAll(list);
    }


    @Override
    public MovieDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_detail_person, parent, false);
        return new MovieDetailHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieDetailHolder holder, int position) {
        Glide.with(holder.avatar.getContext())
                .load(list.get(position).getAvatars().getSmall())
                .placeholder(R.drawable.load_err)
                .error(R.drawable.load_err)
                .crossFade(1500)
                .into(holder.avatar);
        holder.name.setText(list.get(position).getName());
        holder.type.setText(list.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class MovieDetailHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView name;
        TextView type;

        public MovieDetailHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            type = (TextView) itemView.findViewById(R.id.tv_type);
        }
    }
}
