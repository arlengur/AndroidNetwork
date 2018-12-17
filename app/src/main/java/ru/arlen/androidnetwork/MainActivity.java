package ru.arlen.androidnetwork;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ru.arlen.androidnetwork.model.DayWeather;
import ru.arlen.androidnetwork.model.Weather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity implements IActivityCallbacks {
    public static final String POSITION = "position";
    public static final String CITY = "Moscow";
    private RetrofitService mRetrofitService;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRetrofitService = ((RetrofitService.MyBinder) service).getService(MainActivity.this);
            mRetrofitService.getWeather();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRetrofitService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView city = findViewById(R.id.cityTextView);
        city.setText(CITY);

        View refresh = findViewById(R.id.refreshBtn);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRetrofitService.getWeather();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(RetrofitService.newIntent(this), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mServiceConnection);
    }

    private List<String> getDaysTemp(Weather weather) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");

        List<String> listTemp = new ArrayList<>();
        DayWeather[] list = weather.getList();
        for (DayWeather dayWeather : list) {
            String date = df.format(new Date(Long.parseLong(dayWeather.getDt()) * 1000));
            String temp = String.valueOf(dayWeather.getTemp().getDay());
            listTemp.add(date + ": " + temp);
        }
        return listTemp;
    }

    @Override
    public void dataReceived(Weather weather) {
        final ListView titlesList = findViewById(R.id.listDays);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, getDaysTemp(weather));
        titlesList.setAdapter(adapter);
        titlesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(POSITION, String.valueOf(position+1));
                startActivity(intent);
            }
        });
    }
}
