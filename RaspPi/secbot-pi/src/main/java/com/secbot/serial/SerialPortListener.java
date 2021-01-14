package com.secbot.serial;

public interface SerialPortListener {


    void onRead(String rawData);
    void onError(Throwable throwable);

}
