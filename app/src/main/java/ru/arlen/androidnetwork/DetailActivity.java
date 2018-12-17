package ru.arlen.androidnetwork;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;
import ru.arlen.androidnetwork.model.DayTemp;
import ru.arlen.androidnetwork.model.DayWeather;
import ru.arlen.androidnetwork.model.Weather;

import static ru.arlen.androidnetwork.MainActivity.CITY;
import static ru.arlen.androidnetwork.RecyclerWeatherAdapter.POSITION;

public class DetailActivity extends Activity implements IActivityCallbacks {
    private RetrofitService mRetrofitService;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRetrofitService = ((RetrofitService.MyBinder) service).getService(DetailActivity.this);
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
        setContentView(R.layout.activity_detail);

        final TextView city = findViewById(R.id.cityTextView);
        city.setText(CITY);

        View close = findViewById(R.id.closeBtn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this, MainActivity.class));
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
        final String days = getIntent().getStringExtra(POSITION);
        DayWeather dayWeather = weather.getList()[(Integer.parseInt(days) - 1)];
        DayTemp dayTemp = dayWeather.getTemp();

        TextView day = findViewById(R.id.dayTextView);
        TextView min = findViewById(R.id.minTextView);
        TextView max = findViewById(R.id.maxTextView);
        TextView night = findViewById(R.id.nightTextView);
        TextView eve = findViewById(R.id.eveTextView);
        TextView morn = findViewById(R.id.mornTextView);

        day.setText(String.format(getResources().getString(R.string.day_text), dayTemp.getDay()));
        min.setText(String.format(getResources().getString(R.string.min_text), dayTemp.getMin()));
        max.setText(String.format(getResources().getString(R.string.max_text), dayTemp.getMax()));
        night.setText(String.format(getResources().getString(R.string.night_text), dayTemp.getNight()));
        eve.setText(String.format(getResources().getString(R.string.eve_text), dayTemp.getEve()));
        morn.setText(String.format(getResources().getString(R.string.morn_text), dayTemp.getMorn()));
    }
}
