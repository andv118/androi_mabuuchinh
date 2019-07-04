package com.htn.dovanan.mabuuchinh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.htn.dovanan.mabuuchinh.R;
import com.htn.dovanan.mabuuchinh.listener.ItemClickListenerSearch;
import com.htn.dovanan.mabuuchinh.listener.ItemVisibilityView;
import com.htn.dovanan.mabuuchinh.pojo.MbcItem;

import java.util.List;

public class AdapterParent extends RecyclerView.Adapter<AdapterParent.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
//    private static ClickListener clickListener;

    private Context context;
    private List<MbcItem> itemsMBC;

    // listener
    private ItemClickListenerSearch clickIconNext;
    private ItemClickListenerSearch clickMaps;
    private ItemVisibilityView visibilityView;

    // seter
    public void setClickIconNext(ItemClickListenerSearch iconNext) {
        this.clickIconNext = iconNext;
    }

    public void setClickMaps(ItemClickListenerSearch maps) {
        this.clickMaps = maps;
    }

    public void setVisibilityView(ItemVisibilityView visibilityView) {
        this.visibilityView = visibilityView;
    }

    // contructor
    public AdapterParent(Context context, List<MbcItem> itemsMBC) {
        this.context = context;
        this.itemsMBC = itemsMBC;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_mbc_detail, parent, false);
        AdapterParent.ViewHolder viewHolder = new AdapterParent.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        // hien thi buuton next xem chi tiet
        if (visibilityView != null) {
            visibilityView.setVisibilityView(holder.imgNext, position);
        }

        Log.d(TAG, "onBindViewHolder: called");
        final MbcItem mbcItem = itemsMBC.get(position);
        final int id = mbcItem.getId();
        String ten = mbcItem.getTen();
        String mbc = mbcItem.getMabc();
        String data1 = mbcItem.getData1();
        String data2 = mbcItem.getData2();
        String data3 = mbcItem.getData3();
        String data4 = mbcItem.getData4();
        String data5 = mbcItem.getData5();

        String dcn = "Đang cập nhật";
        if (data1 == null) {
            data1 = dcn;
        }
        if (data2 == null) {
            data2 = dcn;
        }
        if (data3 == null) {
            data3 = "Bản đồ";
        }
        if (data4 == null) {
            data4 = dcn;
        }
        if (data5 == null) {
            data5 = dcn;
        }

        holder.txtName.setText(mbc + " - " + ten);
        holder.txtPhone.setText(data1);
        holder.txtLocation.setText(data2);
        holder.txtAddress.setText(data3);
        holder.txtMessage.setText(data4);
        holder.txtLink.setText(data5);

        // set click Listener
        holder.imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickIconNext != null) {
                    clickIconNext.onClickItem(mbcItem, position);
                }
            }
        });

        holder.relativeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickMaps != null) {
                    clickMaps.onClickItem(mbcItem, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsMBC.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtPhone, txtLocation, txtAddress, txtMessage, txtLink;
        private ImageView imgNext;
        private RelativeLayout relativeMap;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txt_ten_tinh);
            txtPhone = (TextView) itemView.findViewById(R.id.txt_phone_tinh);
            txtLocation = (TextView) itemView.findViewById(R.id.txt_location_tinh);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address_tinh);
            txtMessage = (TextView) itemView.findViewById(R.id.txt_message_tinh);
            txtLink = (TextView) itemView.findViewById(R.id.txt_link_tinh);
            imgNext = (ImageView) itemView.findViewById(R.id.img_icon_next);
            relativeMap = (RelativeLayout) itemView.findViewById(R.id.relative_address);
        }
    }
}
