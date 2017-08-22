package app.grandeyardera.com.grandeyardera.util;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by 13118467271 on 2017/8/21.
 */

public class BaiduUtil {
   public static void navigateTo(BaiduMap baiduMap, Location location, boolean isFirstLocate) {
        if (isFirstLocate){
            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            if (update != null) {
                baiduMap.animateMapStatus(update);
            }
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }
    public static LocationListener getLocationListener(final BaiduMap baiduMap, final boolean isFirstLocate){
        return new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub
                if (location != null){
                    BaiduUtil.navigateTo(baiduMap,location,isFirstLocate);
                }
            }
        };
    }


}
