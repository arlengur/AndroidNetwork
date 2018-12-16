package ru.arlen.androidnetwork.model;

import java.util.Arrays;

public class Weather {
    private DayWeather[] list;

    public DayWeather[] getList() {
        return list;
    }

    public void setList(DayWeather[] list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "list=" + Arrays.toString(list) +
                '}';
    }
}
