package com.ssafy.mom.firebase;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mom.config.jwt.JwtService;
import com.ssafy.mom.dao.UserDao;
import com.ssafy.mom.model.BasicResponse;
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
    public Object register(@RequestBody String token, HttpServletRequest request) {
    	final BasicResponse result = new BasicResponse();
        notificationService.register(jwtService.getUserUid(), token);
        result.status=true;
        result.message="토큰 생성완료";
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @PostMapping("/push")
    private Object createReceiveNotification(int receiverUid,HttpServletRequest request) throws InterruptedException, ExecutionException {
    	final BasicResponse result = new BasicResponse();
    	Optional<UserDto> receiver = userDao.findByUid(receiverUid);
        if (receiver.get().isLogin()) {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                .title("POST RECEIVED")
                .token(notificationService.getToken(receiverUid))
                .message("확인")
                .build();
            notificationService.sendNotification(notificationRequest);
            result.status=true;
            result.message="푸쉬완료.";
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        result.status=true;
        result.message="상대가 로그인중이 아닙니다.";
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @DeleteMapping("/logout")
    public Object logout(HttpServletRequest http) {
    	final BasicResponse result = new BasicResponse();
        notificationService.deleteToken(jwtService.getUserUid());
        
        result.status=true;
        result.message="토큰 삭제완료.";
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
