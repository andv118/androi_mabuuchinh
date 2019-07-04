package com.htn.dovanan.mabuuchinh.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.htn.dovanan.mabuuchinh.R;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Util {

    // back stack fragment
    public static String BACK_STACK_FRAGMENT_SEARCH = "Frag_search";
    public static String BACK_STACK_FRAGMENT_PARENT = "Frag_parent";
    public static String BACK_STACK_FRAGMENT_CHILD = "Frag_child";
    public static String BACK_STACK_FRAGMENT_CHILD_2 = "Frag_child_2";

    // don vi tinh
    public static String DONVI_TINH = "tinh";
    public static String DONVI_HUYEN = "huyen";
    public static String DONVI_CT_TINH = "ct_tinh";
    public static String DONVI_CT_HUYEN = "ct_huyen";

    // Bundle put
    public static String ARRAY_TINH = "array_tinh";
    public static String STRING_ALL_ITEM = "all_item";
    public static String OBJECT_ITEM_RECYCLERVIEW = "object_item_mbc";
    public static String BUNDLE_OBJECT = "b_object";
    public static String BUNDLE = "bundle";

    // Key Share preferences
    public  static String PREF_KEY_HUYEN = "pref_key_huyen";
    public  static String PREF_KEY_CT_TINH = "pref_key_ct_tinh";
    public  static String PREF_KEY_CT_HUYEN = "pref_key_ct_huyen";
    public  static String PREF_KEY_LATIUDE = "pref_key_latitude";
    public  static String PREF_KEY_LONGITUDE = "pref_key_longitude";

    // Map Activity
    private static String TAG_MAP_ACTIVITY = "MAP_ACTIVITY";
    public  static String MAP_ACTIVITY_PREF_KEY_MBC = TAG_MAP_ACTIVITY + "_KEY_MBC";
    public  static String MAP_ACTIVITY_PREF_KEY_LATIUDE = TAG_MAP_ACTIVITY + "_KEY_LATIUDE";
    public  static String MAP_ACTIVITY_PREF_KEY_LONGITUDE = TAG_MAP_ACTIVITY + "_KEY_LONGITUDE";
    public  static String MAP_ACTIVITY_PREF_KEY_TITLE_EDT1 = TAG_MAP_ACTIVITY + "_KEY_TITLE_EDT1";
    public  static String MAP_ACTIVITY_PREF_KEY_ADDRESS_EDT1 = TAG_MAP_ACTIVITY + "_KEY_ADDRESS_EDT1";
    public  static String MAP_ACTIVITY_PREF_KEY_TITLE_EDT2 = TAG_MAP_ACTIVITY + "_KEY_TITLE_EDT2";
    public  static String MAP_ACTIVITY_PREF_KEY_ADDRESS_EDT2 = TAG_MAP_ACTIVITY + "_KEY_ADDRESS_EDT2";

    /*
    kiem tra ban phim voi con tro edittetx
    neu edtitext co con tro show keyboard
    ko thi an keyboard
     */
    public static void checkKeyboardWithFocus(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        //If no view currently has focus, create a new one, just so we can grab a window token from it

        // show
        if (view.hasFocus()) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            // hide
        } else {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*
        check connect internet
    */
    public static Boolean haveNetwork(Activity activity) {
        Boolean haveWIFI = false;
        Boolean haveMobiData = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for (NetworkInfo info : networkInfos) {
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    haveWIFI = true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                    haveMobiData = true;
        }

        return haveWIFI || haveMobiData;
    }

}
