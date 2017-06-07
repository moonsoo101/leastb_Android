package com.leastb.moonsoo.walkingeye.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.leastb.moonsoo.walkingeye.DTO.BlackBoxDTO;
import com.leastb.moonsoo.walkingeye.R;
import com.leastb.moonsoo.walkingeye.Util.BitmapDownloaderTask;

import java.util.List;

/**
 * Created by wisebody on 2017. 6. 7..
 */

public class BlackBoxRecyclerAdapter extends RecyclerView.Adapter<BlackBoxRecyclerAdapter.ViewHolder> {
    List<BlackBoxDTO> items;
    int itemLayout;
    Context context;
    public BlackBoxRecyclerAdapter(List<BlackBoxDTO> items, int itemLayout) {
        this.items = items;
        this.itemLayout = itemLayout;
    }

    @Override
    public BlackBoxRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        context = parent.getContext();
        return new ViewHolder(v) ;

    }

    @Override
    public void onBindViewHolder(BlackBoxRecyclerAdapter.ViewHolder holder, int position) {
        BlackBoxDTO item = items.get(position);
        holder.bitmapDownloaderTask = new BitmapDownloaderTask(holder.blackBoxImg,context);
        holder.bitmapDownloaderTask.download("http://ec2-13-124-33-214.ap-northeast-2.compute.amazonaws.com/darknet/"+item.getImgName()+".jpg",holder.blackBoxImg);
//        holder.thumb.setColorFilter(Color.parseColor("#88000000"));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView blackBoxImg;
        BitmapDownloaderTask bitmapDownloaderTask;

        public ViewHolder(View itemView) {
            super(itemView);
            blackBoxImg = (ImageView) itemView.findViewById(R.id.blackBoxImg);
        }
    }

}
