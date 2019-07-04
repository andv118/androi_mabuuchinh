package com.htn.dovanan.mabuuchinh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htn.dovanan.mabuuchinh.R;
import com.htn.dovanan.mabuuchinh.listener.ItemDonloadListenner;
import com.htn.dovanan.mabuuchinh.pojo.ItemDownload;

import java.util.List;

public class DowloadAdapter extends RecyclerView.Adapter<DowloadAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private Context context;
    private List<ItemDownload> listTinh;

    // lítener
    ItemDonloadListenner clicklistener;

    public void setClicklistener(ItemDonloadListenner clicklistener) {
        this.clicklistener = clicklistener;
    }

    public DowloadAdapter(List<ItemDownload> listTinh) {
        this.listTinh = listTinh;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_download, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG,"onBindViewHolder: called");
        final ItemDownload item = listTinh.get(i);
        String tinh = listTinh.get(i).getTinh();
        String url = listTinh.get(i).getUrl();
        viewHolder.txtMBC.setText(tinh);
        // set on click
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicklistener != null) {
                    clicklistener.onClickItem(item, i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTinh.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMBC;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMBC = (TextView) itemView.findViewById(R.id.txtMBC);
        }
    }
}


