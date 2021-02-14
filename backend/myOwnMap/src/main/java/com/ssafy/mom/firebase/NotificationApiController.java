package com.ssafy.mom.firebase;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mom.config.jwt.JwtService;
import com.ssafy.mom.dao.UserDao;
import com.ssafy.mom.model.UserDto;

@RestController
@RequestMapping(value = "/fcm")
public class NotificationApiController {


	@Autowired
	JwtService jwtService;
	
	@Autowired
	UserDao userDao;
	
    private final NotificationService notificationService;

    public NotificationApiController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody String token, HttpServletRequest request) {
        notificationService.register(jwtService.getUserUid(), token);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/push")
    private void createReceiveNotification(int receiverUid,HttpServletRequest request) throws InterruptedException, ExecutionException {
    	Optional<UserDto> receiver = userDao.findByUid(receiverUid);
        if (receiver.get().isLogin()) {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                .title("POST RECEIVED")
                .token(notificationService.getToken(receiverUid))
                .message("확인")
                .build();
            notificationService.sendNotification(notificationRequest);
        }
    }

}
