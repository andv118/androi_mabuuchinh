package com.htn.dovanan.mabuuchinh.presenter.googlemap;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.htn.dovanan.mabuuchinh.model.googlemap.SearchDirectionModel;
import com.htn.dovanan.mabuuchinh.pojo.MbcItem;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class SearchDirectPresenter implements SearchDirectionModel.SearchDirectResponeListener {

    private Context context;
    private SearchDirectListener callback;
    private SearchDirectionModel searchDirectionModel;

    public SearchDirectPresenter(Context context, SearchDirectListener callback) {
        this.context = context;
        this.callback = callback;
        searchDirectionModel = new SearchDirectionModel(context, this);
    }

    public void getDatas(String text) {
        searchDirectionModel.getDataFromWebservice(text);
    }

    /**
     * @param item
     * @param origin         : edittext chọn điểm xuất phát
     * @param destiny        : edittext chọn điểm đến
     * @param isClickTimKiem
     */
    public void clickItemRecyclerView(MbcItem item, View origin, View destiny, Boolean isClickTimKiem) {
        searchDirectionModel.saveAndDisplayMarker(item, origin, destiny, isClickTimKiem);
    }

    public void clickDirection(String text1, String text2, List<Marker> listMarkerDirect) {
        searchDirectionModel.direction(text1, text2, listMarkerDirect);
    }

    public void infoWindowClick(Marker marker, Boolean isClickChiDuong, EditText addressDi, EditText addressDen) {
        searchDirectionModel.infoWindowClick(marker, isClickChiDuong, addressDi, addressDen);
    }

    @Override
    public void onNotHaveNetWork() {
        callback.onNotifiNotHaveNetwork();
    }

    @Override
    public void onFilterSuccess(List<MbcItem> filteredList) {
        callback.onShowDataFilter(filteredList);
    }

    /**
     * set title edittext
     *
     * @param title
     */
    @Override
    public void onOriginSetText(String title) {
        callback.onOriginSetText(title);
    }

    @Override
    public void onDestinySetText(String title) {
        callback.onDestinySetText(title);
    }

    @Override
    public void onAddMarkerSearch(LatLng latLng, String title, String daiChi) {
        callback.onAddMarkerSearch(latLng, title, daiChi);
        callback.onSetUiSearchCallBack();
    }

    @Override
    public void onAddressChuaCapNhat() {
        callback.onToastChuaCapNhat();
    }

    /****************************** Direction ****************************/

    /**
     * Xử lý lấy được điểm bắt đầu, điểm kết thúc
     *
     * @param optionsOrigin
     * @param optionsDest
     */
    @Override
    public void onGetMarkerOnOption(MarkerOptions optionsOrigin, MarkerOptions optionsDest) {
        // add marker map
        callback.onAddMarkerDirections(optionsOrigin, optionsDest);

        // move camera đến điểm xuất phát
        callback.onMoveCameraDirection(optionsOrigin);

        // set Ui Search
        callback.onSetUiSearchDirections();
    }

    /**
     * Sau khi lấy được URL xử lý trên TaskRequest
     *
     * @param url
     */
    @Override
    public void onTaskRequest(String url) {
        callback.onTaskRequestDirections(url);
    }

    /**
     * Chuyển đến app googleMap
     *
     * @param urlConnectGoogleMap: url mở app googleMap
     */
    @Override
    public void onIntentConnectAppGoogleMap(String urlConnectGoogleMap) {
        callback.onIntentConnectAppGoogleMap(urlConnectGoogleMap);
    }

    /**
     * Set text khi click vào infowindow
     * @param editText
     * @param title
     */
    @Override
    public void onSetTextEdtClickInfoWindow(View editText, String title) {
        callback.onSetTextEdtClickInfoWindow(editText, title);
    }

   

    /**
     * Khi chưa điền đầy đủ thông tin
     */
    @Override
    public void onToastDienDayDu() {
        callback.onToastChuaDienDayDu();
    }

    /**
     * Khi không tìm được đường đi
     */
    @Override
    public void onToastChuaTimDuongDi() {
        callback.onToastChuaTimDuocDuongDi();
    }

    /**
     * Vị trí xuất phát chưa chính xác
     */
    @Override
    public void onToastOriginChuaChinhXac() {
        callback.onToasDiemXuatPhatSai();
    }

    /**
     * Vị trí kết thúc chưa chính xác
     */
    @Override
    public void onToastDestinyChuaChinhXac() {
        callback.onToasDiemDenSai();
    }

    /**
     * Nếu 2 vị trí trùng nhau
     */
    @Override
    public void onToast2ViTriTrungNhau() {
        callback.onToast2ViTriTrungNhau();
    }

    /**
     * Listener đưa đến mapActivity
     */
    public interface SearchDirectListener {
        void onNotifiNotHaveNetwork();

        void onShowDataFilter(List<MbcItem> filteredList);

        void onToastChuaCapNhat();

        void onOriginSetText(String title);

        void onDestinySetText(String title);

        void onAddMarkerSearch(LatLng latLng, String title, String daiChi);

        void onSetUiSearchCallBack();

        //////////////////////// Direction
        void onAddMarkerDirections(MarkerOptions optionsOrigin, MarkerOptions optionsDest);

        void onMoveCameraDirection(MarkerOptions optionsOrigin);

        void onSetUiSearchDirections();

        void onTaskRequestDirections(String url);

        void onIntentConnectAppGoogleMap(String urlConnectGoogleMap);

        void onSetTextEdtClickInfoWindow(View editText, String title);

        void onToastChuaDienDayDu();

        void onToasDiemXuatPhatSai();

        void onToasDiemDenSai();

        void onToastChuaTimDuocDuongDi();

        void onToast2ViTriTrungNhau();
    }
}
