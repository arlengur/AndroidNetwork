package ru.arlen.androidnetwork;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.arlen.androidnetwork.model.Weather;

public class RetrofitService extends Service {
    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static final String METRIC_CELSIUS = "metric";
    private static final String USER_KEY = "eb8b1a9405e659b2ffc78f0a520b1a46";
    private static final String CITY = "Moscow";
    private IActivityCallbacks mCallbacks;
    private IBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        RetrofitService getService(IActivityCallbacks callbacks) {
            mCallbacks = callbacks;
            return RetrofitService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    public void getWeather() {
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
                mCallbacks.dataReceived(response.body());
            }
            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());

            }
        });
    }

    public static Intent newIntent(Context context){
        return new Intent(context, RetrofitService.class);
    }
}
