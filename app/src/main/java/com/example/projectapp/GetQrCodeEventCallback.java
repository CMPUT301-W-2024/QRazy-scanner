package com.example.projectapp;

import com.example.projectapp.Model.Event;

public interface GetQrCodeEventCallback {
    void onGetQrCodeEvent(Event event, boolean checkInto, String qrData);
}
