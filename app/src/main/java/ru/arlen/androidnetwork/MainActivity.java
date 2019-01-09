package ru.arlen.androidnetwork;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import ru.arlen.androidnetwork.model.Weather;

public class MainActivity extends Activity implements IActivityCallbacks {
    public static final String CITY = "Moscow";
    private RetrofitService mRetrofitService;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRetrofitService = ((RetrofitService.MyBinder) service).getService();
            Log.w("qwerty", "add");
            mRetrofitService.addCallback(MainActivity.this);
            mRetrofitService.getWeather();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.w("qwerty", "remove");
            mRetrofitService.removeCallback(MainActivity.this);
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

    @Override
    public void dataReceived(Weather weather) {
        final RecyclerView weatherList = findViewById(R.id.recycler);
        weatherList.setAdapter(new RecyclerWeatherAdapter(weather));
    }
}
