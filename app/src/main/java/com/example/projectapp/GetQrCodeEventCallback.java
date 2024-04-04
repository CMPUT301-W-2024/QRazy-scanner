package com.example.projectapp;

public interface GetQrCodeEventCallback {
    void onGetQrCodeEvent(Event event, boolean checkInto, String qrData);
}
