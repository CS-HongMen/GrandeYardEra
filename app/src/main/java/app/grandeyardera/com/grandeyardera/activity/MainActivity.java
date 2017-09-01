package app.grandeyardera.com.grandeyardera.activity;



import android.content.Intent;

import android.location.LocationManager;

import com.amap.api.maps2d.MapView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;



import app.grandeyardera.com.grandeyardera.R;

import app.grandeyardera.com.grandeyardera.util.MapUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    private MapView mapView;
    AMap aMap;
    MapUtil mapUtil;
    private LocationManager locationManager;
    private String provider;
    private boolean isFirstLocate = true;
    private Button scanner;
    private Button userself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.map_view);
        scanner = (Button) findViewById(R.id.scan_qr_code);
        userself = (Button) findViewById(R.id.user);
        aMap = mapView.getMap();
        mapUtil = new MapUtil(mapView,MainActivity.this,savedInstanceState);


        scanner.setOnClickListener(this);
        userself.setOnClickListener(this);
    }
        /**
         * 获取所有可用的位置提供器
         */

    /*
    private void navigateTo(Location location) {

        if(isFirstLocate){
            LocationClientOption option = new LocationClientOption();

            option.setCoorType("bd09ll");
            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            //做缩放级别的调整，3f到19f，返回一个MapStatusUpdate对象
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;

        }

        //显示当前的小光标
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }
    */
    /**
     * 监听位置移动，实时改变位置
     */
    //LocationListener locationListener = BaiduUtil.getLocationListener(baiduMap, isFirstLocate);
    /*
    LocationListener locationListener = new LocationListener(){

        @Override
        public void onLocationChanged(Location location) {

            if(location != null){
                navigateTo(location);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {


        }

        @Override
        public void onProviderEnabled(String provider) {


        }

        @Override
        public void onProviderDisabled(String provider) {


        }

    };
       */

    protected void onPause() {
        super.onPause();
        mapUtil.getMapView().onPause();
    }

    protected void onResume() {
        super.onResume();
        mapUtil.getMapView().onResume();
    }

    protected void onDestroy() {
        super.onDestroy();
        mapUtil.getMapView().onDestroy();
        if(null != mapUtil.getmLocationClient()){
            mapUtil.getmLocationClient().stopLocation();
            mapUtil.getmLocationClient().onDestroy();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan_qr_code:
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.user:
                Intent intentMainToUser = new Intent(MainActivity.this,UserInfoActivity.class);
                startActivity(intentMainToUser);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }

        }
    }
}
