package com.ssafy.mom.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.Gson;
import com.ssafy.mom.config.jwt.JwtService;
import com.ssafy.mom.dao.FCMDao;
import com.ssafy.mom.dao.UserDao;
import com.ssafy.mom.model.BasicResponse;
import com.ssafy.mom.model.FCMRegister;
import com.ssafy.mom.model.Msg;
import com.ssafy.mom.model.UserDto;

@RestController
public class NotificationApiController {

	@Autowired
	FCMDao fcmDao;

	@Autowired
	UserDao userDao;

	@Autowired
	JwtService jwtService;
	


	@PostMapping("/register")
	public Object register(@RequestBody String token, HttpServletRequest http) {
		final BasicResponse result = new BasicResponse();

		int myUid = jwtService.getUserUid();
		Optional<UserDto> user = userDao.findByUid(myUid);

		FCMRegister regi = FCMRegister.builder().userDto(user.get()).token(token).build();
		fcmDao.save(regi);
		result.message = "저장성공";
		result.status = true;
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/sender")
	public Object sender(Map<String, String> mapData, HttpServletRequest http) {
		String result ="";
		int myUid = jwtService.getUserUid();
		int toUid = Integer.parseInt(mapData.get("uid"));
		Optional<UserDto> userFrom = userDao.findByUid(myUid);
		Optional<UserDto> userTo = userDao.findByUid(toUid);
		String to_token = fcmDao.findTokenByUserDto(userTo.get());
		String message = userFrom.get().getUsername() + "님이 " + userTo.get().getUsername() + "님을 "
				+ mapData.get("message") + "하였습니다.";

		// 1. fcm 서버정보 세팅
		String fcm_url = "https://fcm.googleapis.com/fcm/send";
		String content_type = "application/json";
		String server_key = "AAAAb4Mv8pc:APA91bGdlYFDYrv1EfDRyZgNyKSeEclgFj9_sJ1MFzkSVVibKm9yawvBhq9RF5j1y0eiNxiqLNsOTQXKPh7jFYpLvQWZ4yuzzrG5aiqMvxC39SLRzFchG1B9SHVKZJkQBZf1mXIamp38";

		// 2. 메시지정보를 클라이언트(핸드폰)로 부터 수신
		// 위의 함수에 정의된 파라미터에서 값을 받게 된다
		String title = " 보내는사람: " + userFrom.get().getUsername();
		String point = "576434397";

		Msg data = new Msg();
		data.setTo(to_token);
		data.getNotification().setTitle(title);
		data.getNotification().setBody(message);
		data.getNotification().setClick_action("PointActivity");
		data.getData().setPoint(point);
		Gson gson = new Gson();

		// Msg 데이터를 json 스트링으로 변경
		String json_string = gson.toJson(data);
		try {
			// 3.2 HttpUrlConnection 을 사용해서 FCM서버측으로 메시지를 전송한다
			// a.서버연결
			URL url = new URL(fcm_url);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			// b.header 설정
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", "key=" + server_key);
			con.setRequestProperty("Content-Type", content_type);
			// c.POST데이터(body) 전송
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			os.write(json_string.getBytes());
			os.flush();
			os.close();
			// d.전송후 결과처리
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // code 200
				// 결과처리후 FCM 서버측에서 발송한 결과메시지를 꺼낸다.
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String dataLine = "";
				// 메시지를 한줄씩 읽어서 result 변수에 담아두고
				while ((dataLine = br.readLine()) != null) {
					result = result + dataLine;
				}
				br.close();
			}
		} catch (Exception e) {
			result = e.toString();
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@PostMapping("/logout")
	public Object logout(HttpServletRequest request) {
		final BasicResponse result = new BasicResponse();
	    int myUid = jwtService.getUserUid();
	    Optional<UserDto> user = userDao.findByUid(myUid);
	    int fcmNo = fcmDao.findFcmNoByUserDto(user.get());
	    fcmDao.deleteById(fcmNo);
	    result.status=true;
	    result.message="토큰삭제완료";
	    return new ResponseEntity<>(result, HttpStatus.OK);
	}

}