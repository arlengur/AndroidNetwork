package ru.arlen.androidnetwork.model;

public class DayWeather {
    private String dt;
    private DayTemp temp;

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public DayTemp getTemp() {
        return temp;
    }

    public void setTemp(DayTemp temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "DayWeather{" +
                "dt='" + dt + '\'' +
                ", temp=" + temp +
                '}';
    }
}
