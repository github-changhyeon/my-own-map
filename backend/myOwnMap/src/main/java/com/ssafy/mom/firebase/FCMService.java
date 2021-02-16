package com.ssafy.mom.firebase;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;

@Service
public class FCMService {

    private static final Logger logger = LoggerFactory.getLogger(FCMService.class);

    public void send(final NotificationRequest notificationRequest) throws InterruptedException, ExecutionException {
//        Message message = Message.builder()
//                .setToken(notificationRequest.getToken())
//                .setWebpushConfig(WebpushConfig.builder().putHeader("ttl", "300")
//                        .setNotification(new WebpushNotification(notificationRequest.getTitle(),
//                                notificationRequest.getMessage()))
//                        .build())
//                .build();
    	
    	Message message = Message.builder()
                .setToken(notificationRequest.getToken())
//                .setNotification(new Notification(notificationRequest.getTitle(), notificationRequest.getMessage()))
                .setAndroidConfig(AndroidConfig.builder().setNotification(AndroidNotification.builder().setTitle(notificationRequest.getTitle()).setBody(notificationRequest.getMessage()).build()).build())
                .build();

        String response = FirebaseMessaging.getInstance().sendAsync(message).get();
        logger.info("Sent message: " + response);
    }

}