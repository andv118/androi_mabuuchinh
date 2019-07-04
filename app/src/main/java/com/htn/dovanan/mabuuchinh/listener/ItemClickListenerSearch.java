package com.htn.dovanan.mabuuchinh.listener;

import com.htn.dovanan.mabuuchinh.pojo.MbcItem;

/*
    click cho adapter recyclerview khi search tim kiem ma buu chinh
 */
public interface ItemClickListenerSearch {

    public void onClickItem(MbcItem item, int position);
}
