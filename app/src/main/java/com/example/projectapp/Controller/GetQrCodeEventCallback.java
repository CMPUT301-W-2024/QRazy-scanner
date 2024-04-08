package com.example.projectapp.Controller;

import com.example.projectapp.Model.Event;

/**
 * An interface defining a callback mechanism to signal the getting of an qrcode event.
 */
public interface GetQrCodeEventCallback {
    void onGetQrCodeEvent(Event event, boolean checkInto, String qrData);
}
