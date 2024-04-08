package com.example.projectapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.projectapp.Controller.DataHandler;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * The NotificationService class extends FirebaseMessagingService and is responsible for
 * receiving and handling push notifications sent through the Firebase Cloud Messaging (FCM) platform.
 */
public class NotificationService extends FirebaseMessagingService {

    private final String channelId = "CHANNEL_ID_NOTIFICATION";
    private final String channelName = "QrazyScanner";

    /**
     * Called when a new Firebase Cloud Messaging (FCM) message is received. Extracts data
     * from the message payload and triggers a notification.
     *
     * @param message The RemoteMessage object containing the message data.
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (message.getData().size() > 0){
            generateNotification(message.getData().get("event"), message.getData().get("announcement"));
        }
    }

    /**
     * Called when a new Firebase Cloud Messaging (FCM) registration token is generated.  Updates
     * the locally stored Attendee's FCM token in the data store
     *
     * @param token The newly generated FCM token.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        DataHandler dataHandler = DataHandler.getInstance();
        if (dataHandler.getLocalAttendee() != null)
            dataHandler.updateAttendee(dataHandler.getLocalAttendee().getAttendeeId(), "fcmToken", token, null);
    }

    /**
     *  Constructs a notification and displays it to the user.
     *
     *  @param textTitle The title text to be displayed in the notification.
     *  @param textContent The body text to be displayed in the notification.
     */
    private void generateNotification(String textTitle, String textContent){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, builder.build());

    }

}
