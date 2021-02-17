package com.ssafy.mom.firebase;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mom.config.jwt.JwtService;
import com.ssafy.mom.dao.ArticleDao;
import com.ssafy.mom.dao.FavoriteDao;
import com.ssafy.mom.dao.HistoryDao;
import com.ssafy.mom.dao.ProfileImageDao;
import com.ssafy.mom.dao.UserDao;
import com.ssafy.mom.dao.UserFollowDao;
import com.ssafy.mom.model.ArticleDto;
import com.ssafy.mom.model.BasicResponse;
import com.ssafy.mom.model.History;
import com.ssafy.mom.model.UserDto;
import com.ssafy.mom.model.UserFavoriteDto;
import com.ssafy.mom.model.UserFollow;

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

	@Autowired
	UserFollowDao userFollowDao;

	@Autowired
	FavoriteDao favoriteDao;

	@Autowired
	ArticleDao articleDao;
	
	@Autowired
	HistoryDao historyDao;
	
	@Autowired
	ProfileImageDao profileImageDao;

	private final NotificationService notificationService;

	public NotificationApiController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@PostMapping("/register")
	public Object register(@RequestBody String token, HttpServletRequest request) {
		final BasicResponse result = new BasicResponse();
		System.out.println(token + " 토큰토큰 ");
		notificationService.register(jwtService.getUserUid(), token);
		result.status = true;
		result.message = "토큰 생성완료";
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/history")
	public Object retrieveHistory(HttpServletRequest request) {
		final BasicResponse result = new BasicResponse();
		int myUid = jwtService.getUserUid();
		Optional<UserDto> user = userDao.findByUid(myUid);
		if(!user.isPresent()) {
			result.status=false;
			result.message="/fcm/history : 유저가 없어";
			return new ResponseEntity<>(result,HttpStatus.OK);
		}
		List<History> historyList = historyDao.findAllByUserToOrderByRegiTimeDesc(user.get());
		for(int i = 0; i < historyList.size(); ++i) {
			
			 if (profileImageDao.findByUserDto(historyList.get(i).getUserFrom()) != null) {
				 historyList.get(i).getUserFrom().setProfileImagePath(profileImageDao.findByUserDto(historyList.get(i).getUserFrom()).getProfileImage());
             } else {
            	 historyList.get(i).getUserFrom().setProfileImagePath("DefaultProfileImage.png");
             }
			
			}
		System.out.println(historyList.toString()+" /fcm/history-리스트 잘 가져옴?");
		result.status = true;
		result.message = "토큰 생성완료";
		result.object= historyList;
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@PostMapping("/makeZero")
	public Object makeZero(HttpServletRequest request) {
		final BasicResponse result = new BasicResponse();
		int myUid = jwtService.getUserUid();
		Optional<UserDto> user = userDao.findByUid(myUid);
		if(!user.isPresent()) {
			result.status=false;
			result.message="/fcm/makeZero - 유저가 없음";
			return new ResponseEntity<>(result,HttpStatus.OK);
		}
		UserDto userEntity = user.get();
		userEntity.setNotificationCheck(0);
		userDao.save(userEntity);
		result.status=true;
		result.message="/fcm/makeZero - NotificationCheck 0으로 만듬!";
		return new ResponseEntity<>(result,HttpStatus.OK);
	}
	
	@PostMapping("/push")
	private Object createReceiveNotification(@RequestBody Map<String, String> mapDto, HttpServletRequest request)
			throws InterruptedException, ExecutionException {
		System.out.println(mapDto.toString());
		final BasicResponse result = new BasicResponse();
		int receiverUid = Integer.parseInt(mapDto.get("uid"));
		int myUid = jwtService.getUserUid();
		String message = mapDto.get("message");
		Optional<UserDto> sender = userDao.findByUid(myUid);
		Optional<UserDto> receiver = userDao.findByUid(receiverUid);
		History history;
		
		// 팔로우
		if ("FOLLOW".equals(message)) {
			Optional<UserFollow> userFollow = userFollowDao.findByUserFromAndUserTo(sender.get(), receiver.get());
			// 팔로우중이라면
			if (!userFollow.isPresent()) {
				// 취소푸쉬 xx
				result.status = false;
				result.message = "이미 팔로우 중이라 팔로우취소가 되니 푸쉬알림x.";
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
			
			if (receiver.get().isLogin()) {
				sendPush(message, sender.get(), receiver.get());
			}			// 히스토리저장
			history = History.builder()
					.userFrom(sender.get())
					.userTo(receiver.get())
					.state(message)
					.build();
			historyDao.save(history);
			result.status = true;
			result.message = "팔로우푸쉬(히스토리) 완.";
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		// 댓글
		else if ("COMMENT".equals(message)) {
			//해당 댓글의 게시글 찾기
			int articleNo = Integer.parseInt(mapDto.get("articleNo"));
			Optional<ArticleDto> article = articleDao.findByArticleNo(articleNo);
			//게시글이 없다면 오류 출력
			if(!article.isPresent()) {
				System.out.println("오류!오류! : sendPush-게시글없음");
				result.status = false;
				result.message = "오류!오류! 너는 삼류 : sendPush.";
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
			
			// 로그인중이라면 푸쉬알림
			if (receiver.get().isLogin()) {
				sendPush(message, sender.get(), receiver.get());
			}
			
			history = History.builder()
					.userFrom(sender.get())
					.userTo(receiver.get())
					.state(message)
					.articleDto(article.get())
					.build();
			historyDao.save(history);
			result.status = true;
			result.message = "댓글푸쉬(히스토리) 완.";
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		// 찜하기
		else if ("LIKE".equals(message)) {
			int articleNo = Integer.parseInt(mapDto.get("articleNo"));
			Optional<ArticleDto> article = articleDao.findByArticleNo(articleNo);
			Optional<UserFavoriteDto> isFavorite = favoriteDao.findByUserDtoAndArticleDto(sender.get(), article.get());
			// 찜한상태라면
			if(isFavorite.isPresent()) {
				System.out.println();
			}
			if (!isFavorite.isPresent()) {
				// 취소푸쉬는 xx
				result.status = false;
				result.message = "이미 찜하기 중이라 찜하기취소가 되니 푸쉬알림x.";
				return new ResponseEntity<>(result, HttpStatus.OK);
			}

			if (receiver.get().isLogin()) {
				sendPush(message, sender.get(), receiver.get());
			}			// 히스토리저장
			history = History.builder()
					.userFrom(sender.get())
					.userTo(receiver.get())
					.state(message)
					.articleDto(article.get())
					.build();
			historyDao.save(history);
			result.status = true;
			result.message = "찜하기푸쉬(히스토리)완.";
			return new ResponseEntity<>(result, HttpStatus.OK);

		}

		else {
			System.out.println("오류!오류! : sendPush");
			result.status = false;
			result.message = "오류!오류! 너는 삼류 : sendPush.";
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@DeleteMapping("/logout")
	public Object logout(HttpServletRequest request) {
		final BasicResponse result = new BasicResponse();
		int myUid = jwtService.getUserUid();
		notificationService.deleteToken(myUid);
		Optional<UserDto> user = userDao.findByUid(myUid);
		user.get().setLogin(false);
		userDao.save(user.get());
		result.status = true;
		result.message = "토큰 삭제완료.";
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	public void sendPush(String message, UserDto sender, UserDto receiver) {
		//푸쉬하면 히스토리 갯수 추가!
		UserDto user = receiver;
		user.setNotificationCheck(user.getNotificationCheck()+1);
		userDao.save(user);
		
		//message 조건 처리
		if ("FOLLOW".equals(message)) {			
			NotificationRequest notificationRequest = NotificationRequest.builder().title("Follow")
					.token(notificationService.getToken(receiver.getUid()))
					.message(NotificationType.FOLLOW_RECEIVED.generateNotificationMessage(sender, receiver)).build();			
			notificationService.sendNotification(notificationRequest);
		} else if ("LIKE".equals(message)) {
			NotificationRequest notificationRequest = NotificationRequest.builder().title("LIKE")
					.token(notificationService.getToken(receiver.getUid()))
					.message(NotificationType.LIKE_RECEIVED.generateNotificationMessage(sender, receiver)).build();
			notificationService.sendNotification(notificationRequest);
		} else if ("COMMENT".equals(message)) {
			NotificationRequest notificationRequest = NotificationRequest.builder().title("COMMENT")
					.token(notificationService.getToken(receiver.getUid()))
					.message(NotificationType.COMMENT_RECEIVED.generateNotificationMessage(sender, receiver)).build();
			notificationService.sendNotification(notificationRequest);
		}

	}

}
