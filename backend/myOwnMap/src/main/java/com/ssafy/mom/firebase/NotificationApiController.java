package com.ssafy.mom.firebase;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mom.config.jwt.JwtService;
import com.ssafy.mom.dao.UserDao;
import com.ssafy.mom.model.BasicResponse;
import com.ssafy.mom.model.UserDto;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

//@CrossOrigin(origins = { "http://localhost:3000" })
@CrossOrigin(origins = { "*" }, maxAge = 6000)
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
    	System.out.println(token + " 토큰토큰 ");
        notificationService.register(jwtService.getUserUid(), token);
        result.status=true;
        result.message="토큰 생성완료";
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @PostMapping("/push")
    private Object createReceiveNotification(@RequestBody Map<String, String> mapDto, HttpServletRequest request) throws InterruptedException, ExecutionException {
    	
    	// TODO : FOLLOW 했다면 FOLLOW 취소했습니다라는 MESSAGE 출력 (FOLLOW 안한상태만 확인해서 FOLLOW했습니다 출력)
    	
    	final BasicResponse result = new BasicResponse();
    	System.out.println(mapDto.toString());
    	int receiverUid = Integer.parseInt(mapDto.get("uid"));
    	String message = mapDto.get("message");
    	System.out.println(receiverUid + " " + message);
    	Optional<UserDto> receiver = userDao.findByUid(receiverUid);
    	System.out.println(notificationService.getToken(receiverUid) + " service.getToken");
//        if (receiver.get().isLogin()) {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                .title("POST RECEIVED")
                .token(notificationService.getToken(receiverUid))
                .message(message)
                .build();
            notificationService.sendNotification(notificationRequest);
            result.status=true;
            result.message="푸쉬완료.";
            return new ResponseEntity<>(result, HttpStatus.OK);
//        }
//        result.status=true;
//        result.message="상대가 로그인중이 아닙니다.";
//        return new ResponseEntity<>(result, HttpStatus.OK);
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
