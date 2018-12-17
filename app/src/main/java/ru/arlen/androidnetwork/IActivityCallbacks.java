package ru.arlen.androidnetwork;

import ru.arlen.androidnetwork.model.Weather;

public interface IActivityCallbacks {
    void dataReceived(Weather weather);
}
