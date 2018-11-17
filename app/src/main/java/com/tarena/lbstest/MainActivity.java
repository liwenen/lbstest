package com.tarena.lbstest;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.api.analysis.DrivingBehaviorRequest;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.LatestPointRequest;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.ProcessOption;
import com.bumptech.glide.Glide;
import com.tarena.lbstest.gson.Forecast;
import com.tarena.lbstest.gson.Info;
import com.tarena.lbstest.gson.PointsItem;
import com.tarena.lbstest.gson.Weather;
import com.tarena.lbstest.service.AutoUpdateService;
import com.tarena.lbstest.util.CommonUtil;
import com.tarena.lbstest.util.HttpUtil;
import com.tarena.lbstest.util.Utility;
import com.tarena.lbstest.util.Utility1;
import com.tarena.lbstest.util.Utility2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


public class MainActivity extends AppCompatActivity {
    private static final com.baidu.mapapi.map.BitmapDescriptorFactory BitmapDescriptorFactory = null;
    public LocationClient mLocationClient;
    private String mWeatherId;
    private TextView positionText;
    private TextView weather1Text;
    private TextView weather2Text;
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    private Polyline mPolyline;
    private Polyline mPolyline1;
    private String TAG = "MainActivity";
//    private HistoryTrackRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        positionText = (TextView) findViewById(R.id.position_text_view);
        weather1Text = (TextView) findViewById(R.id.weather1);
        weather2Text = (TextView) findViewById(R.id.weather2);
         //requestInfo(0,0);
       requestWeather("0℃");
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivityForResult(intent,1);
            }
        });

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            requestLocation();
        }
    }
@Override
protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK){

                    String returnedData=data.getStringExtra("date1");
                    String returnedData1=data.getStringExtra("date2");
                   // Log.d("MainActivity",returnedData);
                    CommonUtil commonUtil = new CommonUtil();
                    long start_time = commonUtil.toTimeStamp(returnedData);
                    long end_time = commonUtil.toTimeStamp(returnedData1);
                    baiduMap.clear();//清除上一次轨迹信息
                    requestInfo(start_time, end_time);
                }
        }
}
    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
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

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        /*option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);*/
        mLocationClient.setLocOption(option);
    }





    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            if (location.getLocType() == BDLocation.TypeGpsLocation
                    || location.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(location);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder currentPosition = new StringBuilder();
                    currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
                    currentPosition.append("经度：").append(location.getLongitude()).append("\n");
                    currentPosition.append("国家：").append(location.getCountry()).append("\n");
                    currentPosition.append("省：").append(location.getProvince()).append("\n");
                    currentPosition.append("市：").append(location.getCity()).append("\n");
                    currentPosition.append("区：").append(location.getDistrict()).append("\n");
                    currentPosition.append("街道：").append(location.getStreet()).append("\n");
                    currentPosition.append("定位方式：");
                    if (location.getLocType() == BDLocation.TypeGpsLocation) {
                        currentPosition.append("GPS");
                    } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                        currentPosition.append("网络");
                    }
                    positionText.setText(currentPosition);
                }
            });
        }
    }

        public void requestInfo(long start_time,long end_time) {
        String Url = "http://yingyan.baidu.com/api/v3/track/gettrack?"
                +"ak=aWLUHdDeRHMoKOwtVc0WiRzsoUDxnYND&service_id=205637&entity_name=t1&start_time="+start_time+"&end_time="+end_time+"&page_size=1000";
        HttpUtil.sendOkHttpRequest(Url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Info info = Utility1.handleInfoResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        showGuijiInfo(info);
                    }
                });
            }
        });
    }
    private void showGuijiInfo(Info info) {
        PointsItem l[] = info.getPoints();
        if (l == null)
        {
            Log.d(TAG, "showGuijiInfo: 没有获取到数据");
            return;
        }
        List<LatLng> points = new ArrayList<LatLng>();
     //  List<LatLng> points_1 = new ArrayList<LatLng>();
        for (int i = 0; i< l.length-1;i++)
        {
            PointsItem a = l[i];
            PointsItem b = l[i+1];

//            if (b.getLoc_time() - a.getLoc_time() > 300)
//            {
//                LatLng p1_1 = new LatLng(b.getLatitude(), b.getLongitude());
//                points_1.add(p1_1);
//               break;
//            }
                LatLng p1 = new LatLng(a.getLatitude(), a.getLongitude());
                points.add(p1);
        }
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(points);
      // OverlayOptions ooPolyline1 = new PolylineOptions().width(10)
       //         .color(0xAAFF0000).points(points_1);
        mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline);
     //  mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline1);

    }
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=CN101120901&key=26adf0e3f4ce4810bbf86de0ab8d97f5";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                            showWeatherInfo(weather);


                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "获取天气信息失败",
                                Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }

    private void showWeatherInfo(Weather weather) {

        String degree = "临沂市  "+weather.now.temperature + "℃";
        String txt=weather.now.more.info;
        weather1Text.setText(degree);
        weather2Text.setText(txt);


    }
}

