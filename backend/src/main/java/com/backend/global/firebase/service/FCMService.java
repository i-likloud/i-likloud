package com.backend.global.firebase.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

    public void sengFCMNotification(String token, String title, String body) {
        try{
            // 알림 내용 설정
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            // 알림 보낼 대상 설정
            Message message = Message.builder()
                    .setNotification(notification)
                    .setToken(token)
                    .build();

            // FCM으로 알림 전송
            String response = FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            e.printStackTrace();;
        }
    }
}
