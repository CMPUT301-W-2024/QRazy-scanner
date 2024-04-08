package com.example.projectapp.Controller;

/**
 * An interface defining a callback mechanism to signal the getting of a Qr code.
 */
public interface GetQrCodeCallback {
    void onGetQrCode(String qrCode);
}
