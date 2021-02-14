package com.ssafy.mom.config;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.ssafy.mom.controller.NotificationApiController;

public class FCMinit {
	private static final Logger logger = LoggerFactory.getLogger(NotificationApiController.class);
	private static final String FIREBASE_CONFIG_PATH = "myownmap-304803-firebase-adminsdk-1gswq-f58a020d7f.json";

	@PostConstruct
	public void initialize() {
		try {
			FirebaseOptions options = new FirebaseOptions.Builder()
					// 다운받은 비밀키를 가져와서 증명한다.
					.setCredentials(
							GoogleCredentials.fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream()))
					.build();
			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
				logger.info("Firebase application has been initialized");
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
}
