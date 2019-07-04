package com.htn.dovanan.mabuuchinh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htn.dovanan.mabuuchinh.R;
import com.htn.dovanan.mabuuchinh.listener.ItemClickListenerSearch;
import com.htn.dovanan.mabuuchinh.pojo.MbcItem;

import java.util.List;

public class AdapterSearchMap extends RecyclerView.Adapter<AdapterSearchMap.ViewHolder>{

    private static final String TAG = "Adapter_Search_Map";
    private List<MbcItem> listMbc;
    private Context context;

    // listener
    ItemClickListenerSearch clicklistener;
    public void setClicklistener(ItemClickListenerSearch clicklistener) {
        this.clicklistener = clicklistener;
    }

    public AdapterSearchMap(Context context , List<MbcItem> listMbc) {
        this.listMbc = listMbc;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_search_map, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
//        Log.d(TAG,"onBindViewHolder: called");
        final MbcItem itemSearch = listMbc.get(i);
        String title = itemSearch.getMabc() + " _ " + itemSearch.getTen();
        viewHolder.txtTtile.setText(title);
        String snippet = itemSearch.getData3();
        if (snippet == null) {
            snippet = "địa chỉ: chưa cập nhật";
        }
        viewHolder.txtSnippet.setText(snippet);
        // set on click
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicklistener != null) {
                    clicklistener.onClickItem(itemSearch, i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMbc.size();
    }

    /*    Loc du lieu */
    public void filterList(List<MbcItem> filterList) {
        listMbc = filterList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTtile, txtSnippet;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTtile = (TextView) itemView.findViewById(R.id.txtHeader);
            txtSnippet = (TextView) itemView.findViewById(R.id.txtSnippet);
        }
    }
}
