package ru.arlen.androidnetwork;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ru.arlen.androidnetwork.model.DayWeather;
import ru.arlen.androidnetwork.model.Weather;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecyclerWeatherAdapter extends RecyclerView.Adapter<RecyclerWeatherAdapter.Holder> {
    public static final String POSITION = "position";
    private Weather mWeather;

    public RecyclerWeatherAdapter(Weather weather) {
        mWeather = weather;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new Holder(inflater.inflate(R.layout.item_weather, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        DayWeather dayWeather = mWeather.getList()[i];
        String date = df.format(new Date(Long.parseLong(dayWeather.getDt()) * 1000));
        String temp = String.valueOf(dayWeather.getTemp().getDay());

        holder.bind(date + ": " + temp, i + 1);
    }

    @Override
    public int getItemCount() {
        return mWeather.getList().length;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView weatherItem;

        public Holder(@NonNull View itemView) {
            super(itemView);
            weatherItem = itemView.findViewById(R.id.weatherItem);

            weatherItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(POSITION, String.valueOf(v.getTag()));
                    context.startActivity(intent);
                }
            });
        }

        public void bind(String text, int id) {
            weatherItem.setText(text);
            weatherItem.setTag(id);
        }
    }
}
