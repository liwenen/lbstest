package com.tarena.lbstest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.tarena.lbstest.gson.Info;
import com.tarena.lbstest.gson.PointsItem;
import com.tarena.lbstest.util.CommonUtil;
import com.tarena.lbstest.util.HttpUtil;
import com.tarena.lbstest.util.Utility1;

import java.io.IOException;

import java.util.Calendar;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity
        implements View.OnClickListener, DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {
    private TextView textView1;
    private TextView textView;
    private Context context;
    private LinearLayout llDate, llTime,llTime2;
    private TextView tvDate, tvTime,tvTime2;
    private int year, month =11, day, hour, minute,hour1,minute1,hour2,minute2;
    private long startTime = CommonUtil.getCurrentTime();
    private long endTime = CommonUtil.getCurrentTime();
    //在TextView上显示的字符
    private StringBuffer date, time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView1 = (TextView) findViewById(R.id.edit_text);
        textView = (TextView) findViewById(R.id.edit1_text);
        context = this;
        date = new StringBuffer();
        time = new StringBuffer();
        initView();
        initDateTime();
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
                startActivity(intent);
            }
        });
        Button button2=(Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //month++;
                String date1 = year + "-" + month+"-"+day+" "+hour1+":"+minute1+":" + "00";
                String date2 = year + "-" + month+"-"+day+" "+hour2+":"+minute2+":" + "00";
                Intent intent=new Intent();
                intent.putExtra("date1",date1);
                intent.putExtra("date2",date2);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        requestInfo(0,0);//xianguanshang
    }
    private void initView() {

        llDate = (LinearLayout) findViewById(R.id.ll_date);
        tvDate = (TextView) findViewById(R.id.tv_date);
        llTime = (LinearLayout) findViewById(R.id.ll_time);
        tvTime = (TextView) findViewById(R.id.tv_time);
        llTime2 = (LinearLayout) findViewById(R.id.ll_time2);
        tvTime2 = (TextView) findViewById(R.id.tv_time2);
        llDate.setOnClickListener(this);
        llTime.setOnClickListener(this);
        llTime2.setOnClickListener(this);
    }

    /**
     * 获取当前的日期和时间
     */
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
//        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_date:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (date.length() > 0) { //清除上次记录的日期
                            date.delete(0, date.length());
                        }
                        tvDate.setText(date.append(String.valueOf(year)).append("年").
                                append(String.valueOf(month )).append("月").append(day).append("日"));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(context, R.layout.dialog_date, null);
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
                dialog.setTitle("起始日期");
                dialog.setView(dialogView);
                dialog.show();
                //初始化日期监听事件
                datePicker.init(year, month - 1, day, this);
                break;
            case R.id.ll_time:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (time.length() > 0) { //清除上次记录的日期
                            time.delete(0, time.length());
                        }
                        hour1=hour;
                        minute1=minute;
                        tvTime.setText(time.append(String.valueOf(hour)).append("时").append(String.valueOf(minute)).append("分"));
                        dialog.dismiss();
                    }
                });
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog2 = builder2.create();
                View dialogView2 = View.inflate(context, R.layout.alter_dialog, null);
                TimePicker timePicker = (TimePicker) dialogView2.findViewById(R.id.timePicker);
                timePicker.setCurrentHour(hour);
                timePicker.setCurrentMinute(minute);
                timePicker.setIs24HourView(true); //设置24小时制
                timePicker.setOnTimeChangedListener(this);
                dialog2.setTitle("设置时间");
                dialog2.setView(dialogView2);
                dialog2.show();
                break;
            case R.id.ll_time2:
                AlertDialog.Builder builder3 = new AlertDialog.Builder(context);
                builder3.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (time.length() > 0) { //清除上次记录的日期
                            time.delete(0, time.length());
                        }
                        hour2=hour;
                        minute2=minute;
                        tvTime2.setText(time.append(String.valueOf(hour)).append("时").append(String.valueOf(minute)).append("分"));
                        dialog.dismiss();
                    }
                });
                builder3.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog3 = builder3.create();
                View dialogView3 = View.inflate(context, R.layout.alter_dialog, null);
                TimePicker timePicker1 = (TimePicker) dialogView3.findViewById(R.id.timePicker);
                timePicker1.setCurrentHour(hour);
                timePicker1.setCurrentMinute(minute);

                timePicker1.setIs24HourView(true); //设置24小时制
                timePicker1.setOnTimeChangedListener(this);
                dialog3.setTitle("结束时间");
                dialog3.setView(dialogView3);
                dialog3.show();
                break;
        }

    }


    /**
     * 日期改变的监听事件
     *
     * @param view
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
//        this.month = monthOfYear;
       this.day = dayOfMonth;
    }

    /**
     * 时间改变的监听事件
     *
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
    }

    public void requestInfo(long start_time,long end_time) {
        String Url = "http://yingyan.baidu.com/api/v3/track/gettrack?ak=aWLUHdDeRHMoKOwtVc0WiRzsoUDxnYND&service_id=205637&entity_name=t1&start_time=1542153600&end_time=1542211200";
        HttpUtil.sendOkHttpRequest(Url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
              Toast.makeText(Main2Activity.this, "Failed", Toast.LENGTH_SHORT).show();
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
    //   double i = info.getDistance();
        String i= "路程: " +Double.toString(info.getDistance());
      //  String v="速度： "+Double.toString(PointsItem.getSpeed());
        PointsItem l[] = info.getPoints();
      String v= "速度：    "  + Double.toString(l[l.length-1].getSpeed());
     textView1.setText(i);
     textView.setText(v);
    }
}