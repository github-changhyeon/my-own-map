package com.ssafy.mom.firebase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	@Autowired
	FCMService fcmService;
	
	private final Map<Integer, String> tokenMap = new HashMap<>();
    
    
 
    public void register(final int userId, final String token) {
            tokenMap.put(userId, token);
    }

	public String getToken(int receiverUid) {
		
		return tokenMap.get(receiverUid);
	}

	public void sendNotification(NotificationRequest notificationRequest) throws InterruptedException, ExecutionException {
		fcmService.send(notificationRequest);
		
	}

	public void deleteToken(int userUid) {
		tokenMap.remove(userUid);		
	}
}