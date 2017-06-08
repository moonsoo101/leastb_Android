package com.leastb.moonsoo.walkingeye.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leastb.moonsoo.walkingeye.BlackBoxActivity;
import com.leastb.moonsoo.walkingeye.DTO.BlackBoxDTO;
import com.leastb.moonsoo.walkingeye.DTO.WatchDayDTO;
import com.leastb.moonsoo.walkingeye.MapActivity;
import com.leastb.moonsoo.walkingeye.MapViewDemoActivity;
import com.leastb.moonsoo.walkingeye.PolygonDemoActivity;
import com.leastb.moonsoo.walkingeye.R;
import com.leastb.moonsoo.walkingeye.Util.BitmapDownloaderTask;

import java.util.List;

/**
 * Created by wisebody on 2017. 6. 7..
 */

public class WatchDayAdapter extends RecyclerView.Adapter<WatchDayAdapter.ViewHolder> {
    List<WatchDayDTO> items;
    int itemLayout;
    Context context;
    public WatchDayAdapter(List<WatchDayDTO> items, int itemLayout) {
        this.items = items;
        this.itemLayout = itemLayout;
    }

    @Override
    public WatchDayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        context = parent.getContext();
        return new ViewHolder(v) ;

    }

    @Override
    public void onBindViewHolder(final WatchDayAdapter.ViewHolder holder, int position) {
        WatchDayDTO item = items.get(position);
       holder.date.setText(item.getDate());
        holder.count.setText(item.getCount());
        holder.watchDayCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PolygonDemoActivity.class);
                intent.putExtra("id",items.get(holder.getAdapterPosition()).getId());
                context.startActivity(intent);
                Log.d("ssibal",Integer.toString(items.get(holder.getAdapterPosition()).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout watchDayCont;
        TextView date, count;

        public ViewHolder(View itemView) {
            super(itemView);
            watchDayCont = (RelativeLayout) itemView.findViewById(R.id.watchDayCont);
            date = (TextView) itemView.findViewById(R.id.date);
            count = (TextView) itemView.findViewById(R.id.count);
        }
    }

}
