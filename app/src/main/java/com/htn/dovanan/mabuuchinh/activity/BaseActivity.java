package com.htn.dovanan.mabuuchinh.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private AlertDialog dialogCustom;

    private Context baseContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(injectLayout());

        baseContext = BaseActivity.this;
//        initailView();
//        initialVariable();

        // create dialog
//        dialog = new SpotsDialog(baseContext, R.style.CustomDialog);

    }

    /**
     * @return layout ma hien thi cho activity
     */
//    public abstract int injectLayout();

    /**
     * Dung de khoi tao cai view
     */
//    public abstract void initailView();

    /**
     * Khoi tao cac bien, tham so gan gia tri cho cac views
     */
//    public abstract void initialVariable();

    /**
     *
     */
    public void showProgressDialog(){
        try{
            if (dialog == null) {
                dialog = new ProgressDialog(baseContext);
                dialog.setCancelable(false);
                dialog.show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void closeProgressDialog(){
        try{
            if (dialog != null) {
                dialog.cancel();
                dialog = null;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
