package com.ssafy.mom.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mom.dao.UserFollowDao;
import com.ssafy.mom.config.jwt.JwtService;
import com.ssafy.mom.dao.UserDao;
import com.ssafy.mom.model.BasicResponse;
import com.ssafy.mom.model.SendUserInfo;
import com.ssafy.mom.model.UserDto;
import com.ssafy.mom.model.UserFollow;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

//@CrossOrigin(origins = { "http://localhost:3000" })
@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping(value = "/follow")
@RequiredArgsConstructor
public class FollowController {

	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Autowired
	UserFollowDao userFollowDao;

	@Autowired
	UserDao userDao;

	@Autowired
	JwtService jwtService;

	@GetMapping("/doFollow/{uid}")
	@ApiOperation(value = "팔로우하기")
	public Object follow(@PathVariable @ApiParam(value = "팔로우 시 필요한 회원정보(상대의 이메일).", required = true) int uid,
			HttpServletRequest request) {
		final BasicResponse result = new BasicResponse();

		int myUid = jwtService.getUserUid();
		Optional<UserDto> userFrom = userDao.findByUid(myUid);

		Optional<UserDto> userTo = userDao.findByUid(uid);

		// 이미 내가 팔로우하였는지 확인!
		List<UserFollow> userEntity = userFollowDao.findAllByUserFrom(userFrom.get());
		// 팔로우한 사람이 있는가?
		if (!userEntity.isEmpty()) {
			for (int i = 0; i < userEntity.size(); i++) {
				// 이미 팔로우 중인데 클릭했으므로 삭제
				if (userEntity.get(i).getUserTo().getEmail() == userTo.get().getEmail()) {
					userFollowDao.deleteByUserFromAndUserTo(userFrom.get(), userEntity.get(i).getUserTo());
					result.status = true;
					result.message = "팔로우가 취소되었습니다.";
					result.object = retrieveUserFollower(uid);
					return new ResponseEntity<>(result, HttpStatus.OK);
				}
			}
		}

		UserFollow userFollow = UserFollow.builder().userFrom(userFrom.get()).userTo(userTo.get()).build();
		userFollowDao.save(userFollow);
		result.status = true;
		result.message = "팔로우를 성공하였습니다";
		result.object = retrieveUserFollower(uid);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// List<UserDto>로 보내주기
	// 내가 팔로우한 사람(팔로잉)
	@GetMapping("/findFollowing/{uid}")
	@ApiOperation(value = "{uid}가 팔로우한 사람")
	public Object retrieveUserFollowing(
			@ApiParam(value = "{uid}이 팔로우한 사람들(팔로잉) List<UserDto>로 반환", required = true) @PathVariable int uid) {
		final BasicResponse result = new BasicResponse();
		
		Optional<UserDto> userFrom = userDao.findByUid(uid);
		List<UserFollow> userEntity = userFollowDao.findAllByUserFrom(userFrom.get());
		List<SendUserInfo> userList = new ArrayList<>();
		for (int i = 0; i < userEntity.size(); i++) {
			UserDto userBuilder = userEntity.get(i).getUserTo();
	
			SendUserInfo user = SendUserInfo.builder()
					.uid(userBuilder.getUid())
					.email(userBuilder.getEmail())
					.username(userBuilder.getUsername())
					.stateMsg(userBuilder.getStateMsg())
					.build();
			userList.add(user);
		}
		result.status = true;
		result.message = "팔로잉리스트입니다.";
		result.object = userList;
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	// 나를 팔로우한 사람(팔로워)
	@GetMapping("/findFollower/{uid}")
	@ApiOperation(value = "{uid}를 팔로우한 사람")
	public Object retrieveUserFollower(@ApiParam(value = "{uid}, {uid}를 팔로우한(팔로워) List<UserDto>로 반환", required = true) @PathVariable int uid) {
		final BasicResponse result = new BasicResponse();
	
		Optional<UserDto> userTo = userDao.findByUid(uid);
		List<UserFollow> userEntity = userFollowDao.findAllByUserTo(userTo.get());
		List<SendUserInfo> userList = new ArrayList<>();
		for (int i = 0; i < userEntity.size(); i++) {
			UserDto userBuilder = userEntity.get(i).getUserFrom();		
			SendUserInfo user = SendUserInfo.builder()
					.uid(userBuilder.getUid())
					.email(userBuilder.getEmail())
					.username(userBuilder.getUsername())
					.stateMsg(userBuilder.getStateMsg())
					.build();
			userList.add(user);
		}
		result.status = true;
		result.message = "팔로워리스트입니다.";
		result.object = userList;
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	//현재 password등 필요없는 여러개도 다같이보냄.
	//id username,email, profileImg, 만 넘겨주기
	//stateMsg 추가해주기!
	
	//현재 팔로우되잇는지확인하기위한 상태체크
	@GetMapping("/isFollow/{uid}")
	@ApiOperation(value = "jwt가 uid를 팔로우중인지 체크")
	public Object isFollow(@PathVariable int uid, HttpServletRequest request) {
		final BasicResponse result = new BasicResponse();
		int myUid = jwtService.getUserUid();
		Optional<UserDto> userFrom = userDao.findByUid(myUid);
		Optional<UserDto> userTo = userDao.findByUid(uid);
		Optional<UserFollow> isFollow = userFollowDao.findByUserFromAndUserTo(userFrom.get(), userTo.get());
		if(isFollow.isPresent()) {
			result.status= true;
			result.message="팔로우중입니다";
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		else {
			result.status=false;
			result.message="팔로우중이 아닙니다.";
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}
}
