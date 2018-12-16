package ru.arlen.androidnetwork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.arlen.androidnetwork.model.DayTemp;
import ru.arlen.androidnetwork.model.DayWeather;
import ru.arlen.androidnetwork.model.Weather;

import static ru.arlen.androidnetwork.MainActivity.*;

public class DetailActivity extends Activity {
    public static final String POSITION = "position";
    public static final String DETAIL_ACTIVITY_TAG = "DetailActivity";
    private Weather weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final TextView city = findViewById(R.id.cityTextView);
        city.setText(CITY);
        final TextView day = findViewById(R.id.dayTextView);
        final TextView min = findViewById(R.id.minTextView);
        final TextView max = findViewById(R.id.maxTextView);
        final TextView night = findViewById(R.id.nightTextView);
        final TextView eve = findViewById(R.id.eveTextView);
        final TextView morn = findViewById(R.id.mornTextView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final String days = getIntent().getStringExtra(POSITION);
        WebAPI webAPI = retrofit.create(WebAPI.class);
        final Call<Weather> weatherCall = webAPI
                .getWeather(CITY, METRIC_CELSIUS, days, USER_KEY);
        weatherCall.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                weather = response.body();
                DayWeather dayWeather = weather.getList()[(Integer.parseInt(days)-1)];
                DayTemp dayTemp = dayWeather.getTemp();

                day.setText(String.format(getResources().getString(R.string.day_text), dayTemp.getDay()));
                min.setText(String.format(getResources().getString(R.string.min_text), dayTemp.getMin()));
                max.setText(String.format(getResources().getString(R.string.max_text), dayTemp.getMax()));
                night.setText(String.format(getResources().getString(R.string.night_text), dayTemp.getNight()));
                eve.setText(String.format(getResources().getString(R.string.eve_text), dayTemp.getEve()));
                morn.setText(String.format(getResources().getString(R.string.morn_text), dayTemp.getMorn()));
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.w(DETAIL_ACTIVITY_TAG, t.getMessage());

            }
        });

        View close = findViewById(R.id.closeBtn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this, MainActivity.class));
            }
        });
    }
}
