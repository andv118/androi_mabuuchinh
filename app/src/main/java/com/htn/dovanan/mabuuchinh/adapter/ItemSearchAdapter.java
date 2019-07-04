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
import com.htn.dovanan.mabuuchinh.listener.ItemClickListenerSearch;
import com.htn.dovanan.mabuuchinh.pojo.MbcItem;

import java.util.List;

public class ItemSearchAdapter extends RecyclerView.Adapter<ItemSearchAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private Context context;
    private List<MbcItem> itemsMBC;

    // listener
    ItemClickListenerSearch clicklistener;

    public void setClicklistener(ItemClickListenerSearch clicklistener) {
        this.clicklistener = clicklistener;
    }

    public ItemSearchAdapter(Context context, List<MbcItem> itemsMBC) {
        this.context = context;
        this.itemsMBC = itemsMBC;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_items, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG,"onBindViewHolder: called");
        final MbcItem itemSearch = itemsMBC.get(i);
        String mbcItem = itemSearch.getMabc() + " - " + itemSearch.getTen();
        viewHolder.txtMBC.setText(mbcItem);
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
        return itemsMBC.size();
    }

    /*
        Loc du lieu
     */
    public void filterList(List<MbcItem> filterList) {
        itemsMBC = filterList;
        notifyDataSetChanged();
    }

    public List<MbcItem> getAllItem() {
        return itemsMBC;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMBC;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMBC = (TextView) itemView.findViewById(R.id.txtMBC);
        }
    }
}


