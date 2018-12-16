package ru.arlen.androidnetwork;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.arlen.androidnetwork.model.DayWeather;
import ru.arlen.androidnetwork.model.Weather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {
    public static final String POSITION = "position";
    public static final String BASE_URL = "https://api.openweathermap.org/";
    public static final String METRIC_CELSIUS = "metric";
    public static final String USER_KEY = "eb8b1a9405e659b2ffc78f0a520b1a46";
    public static final String CITY = "Moscow";
    private Weather weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView city = findViewById(R.id.cityTextView);
        city.setText(CITY);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebAPI webAPI = retrofit.create(WebAPI.class);
        final Call<Weather> weatherCall = webAPI
                .getWeather(CITY, METRIC_CELSIUS, "10", USER_KEY);
        weatherCall.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                weather = response.body();
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

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());

            }
        });
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
}
