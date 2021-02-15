package com.ssafy.mom.firebase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
	
	@Autowired
	FCMService fcmService;
	
	private final Map<Integer, String> tokenMap = new HashMap<>();
    
    
 
    public void register(final int userId, final String token) {
            tokenMap.put(userId, token);
    		System.out.println(tokenMap.get(userId) + "풋토큰");

    }

	public String getToken(int receiverUid) {
		
		System.out.println(tokenMap.get(receiverUid) + "겟토큰");
		return tokenMap.get(receiverUid);
	}

	public void sendNotification(final NotificationRequest request) {
        try {
            fcmService.send(request);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }

	public void deleteToken(int userUid) {
		tokenMap.remove(userUid);		
	}
	
	
}