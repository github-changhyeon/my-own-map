package com.ssafy.mom.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.mom.config.jwt.JwtServiceImpl;
import com.ssafy.mom.dao.ArticleDao;
import com.ssafy.mom.dao.ArticleHashtagDao;
import com.ssafy.mom.dao.ImageDao;
import com.ssafy.mom.dao.ProfileImageDao;
import com.ssafy.mom.dao.UserDao;
import com.ssafy.mom.dao.UserHashtagDao;
import com.ssafy.mom.email.EmailService;
import com.ssafy.mom.model.ArticleDto;
import com.ssafy.mom.model.ArticleHashtag;
import com.ssafy.mom.model.BasicResponse;
import com.ssafy.mom.model.HashtagDto;
import com.ssafy.mom.model.ImageDto;
import com.ssafy.mom.model.ProfileImageDto;
import com.ssafy.mom.model.UserDto;
import com.ssafy.mom.model.UserHashtag;

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
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Value("${file.path}")
	private String fileRealPath;

	@Autowired
	private ArticleDao articleDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	EmailService emailService;

	@Autowired
	private JwtServiceImpl jwtService;

	@Autowired
	private ArticleHashtagDao articleHashtagDao;

	@Autowired
	private ImageDao imageDao;

	@Autowired
	private UserHashtagDao userHashtagDao;

	@Autowired
	private ProfileImageDao profileImageDao;

	
//	@ApiOperation(value = "이메일 중복 확인", response = List.class)
//	@PostMapping("/checkDuplicatedEmail")
//	public ResponseEntity<BasicResponse> checkDuplicatedEmail(@RequestParam(value = "email", required = false) String email){
//		final BasicResponse result = new BasicResponse();
//		result.status = true;
//		result.message = "success";
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}
	
	// 나의 메인페이지
	@ApiOperation(value = "해당 유저의 최신 게시물 10개를 받아온다", response = List.class)
	@GetMapping("/{uid}/recentArticles")
	public ResponseEntity<BasicResponse> retrieveNewTenArticle(@PathVariable String uid) {
		Optional<UserDto> userOpt = userDao.findByUid(Integer.parseInt(uid));
		List<ArticleDto> articles = articleDao.findTop10ByUserDtoOrderByUpdateTimeDesc(userOpt.get());
		for (int i = 0; i < articles.size(); ++i) {
			List<ArticleHashtag> articleHashtags = articleHashtagDao.findAllByArticleDto(articles.get(i));
			ArrayList<HashtagDto> tmpHashtags = new ArrayList<>();
			for (int j = 0; j < articleHashtags.size(); ++j) {
				tmpHashtags.add(articleHashtags.get(j).getHashtagDto());
			}

			articles.get(i).setHashtags(tmpHashtags);
			List<ImageDto> tmpImages = imageDao.findAllByArticleDto(articles.get(i));
			ArrayList<String> tmpImagePaths = new ArrayList<>();
			if(tmpImages.size() != 0) {
				for (int j = 0; j < tmpImages.size(); j++) {
					tmpImagePaths.add(tmpImages.get(j).getPostImage());
				}
			}else {
				tmpImagePaths.add("DefaultProfileImage.png");
			}
			articles.get(i).setImagePaths(tmpImagePaths);
//			articles.get(i).setUid(userOpt.get().getUid());
			articles.get(i).setUserDto(userOpt.get());
		}

		final BasicResponse result = new BasicResponse();
		result.status = true;
		result.message = "success";
		result.object = articles;
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// ------------------공개/비공개 게시물!
	@ApiOperation(value = "해당 유저의 공개된 최신 게시물 10개를 받아온다", response = List.class)
	@GetMapping("/{uid}/recentPublicArticles")
	public ResponseEntity<BasicResponse> retrieveNewTenPublicArticle(@PathVariable String uid) {
		Optional<UserDto> userOpt = userDao.findByUid(Integer.parseInt(uid));
		List<ArticleDto> articles = articleDao.findTop10ByUserDtoAndIsPrivateOrderByUpdateTimeDesc(userOpt.get(),
				false);
		for (int i = 0; i < articles.size(); ++i) {
			List<ArticleHashtag> articleHashtags = articleHashtagDao.findAllByArticleDto(articles.get(i));
			ArrayList<HashtagDto> tmpHashtags = new ArrayList<>();
			for (int j = 0; j < articleHashtags.size(); ++j) {
				tmpHashtags.add(articleHashtags.get(j).getHashtagDto());
			}

			articles.get(i).setHashtags(tmpHashtags);
			List<ImageDto> tmpImages = imageDao.findAllByArticleDto(articles.get(i));
			ArrayList<String> tmpImagePaths = new ArrayList<>();
			if(tmpImages.size() != 0)
			{
				for (int j = 0; j < tmpImages.size(); j++) {
					tmpImagePaths.add(tmpImages.get(j).getPostImage());
				}
			}else {
				tmpImagePaths.add("DefaultProfileImage.png");
			}
			articles.get(i).setImagePaths(tmpImagePaths);
//			articles.get(i).setUid(userOpt.get().getUid());
			articles.get(i).setUserDto(userOpt.get());
		}

		final BasicResponse result = new BasicResponse();
		result.status = true;
		result.message = "success";
		result.object = articles;
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@ApiOperation(value = "해당 유저의 해쉬태그를 모두 반환한다.", response = List.class)
	@GetMapping("/{uid}/userHashtags")
	public ResponseEntity<BasicResponse> retrieveHashtags(@PathVariable String uid) {

		Optional<UserDto> userOpt = userDao.findByUid(Integer.parseInt(uid));
		List<UserHashtag> list = userHashtagDao.findAllByUserDto(userOpt.get());

		List<HashtagDto> hashtags = new ArrayList<HashtagDto>();
		for (int i = 0; i < list.size(); i++) {
			hashtags.add(list.get(i).getHashtagDto());
		}

		final BasicResponse result = new BasicResponse();
		result.status = true;
		result.message = "success";
		result.object = hashtags;
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 공개된 해쉬태그 만들기!!!!!
	@ApiOperation(value = "해당 유저의 공개된 해쉬태그를 모두 반환한다.", response = List.class)
	@GetMapping("/{uid}/userPublicHashtags")
	public ResponseEntity<BasicResponse> retrievePublicHashtags(@PathVariable String uid) {
		
		Optional<UserDto> userOpt = userDao.findByUid(Integer.parseInt(uid));
		List<UserHashtag> list = userHashtagDao.findAllByUserDtoAndPublicCntGreaterThan(userOpt.get(), 0);
		
		List<HashtagDto> hashtags = new ArrayList<HashtagDto>();
		for (int i = 0; i < list.size(); i++) {
			hashtags.add(list.get(i).getHashtagDto());
		}
		
		final BasicResponse result = new BasicResponse();
		result.status = true;
		result.message = "success";
		result.object = hashtags;
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// private + public -> 메인페이지에서 사용!
	@ApiOperation(value = "해당 유저의 모든 게시글을 반환한다", response = List.class)
	@GetMapping("/{uid}/articles")
	public ResponseEntity<BasicResponse> retrieveArticles(@PathVariable String uid) {

		Optional<UserDto> userOpt = userDao.findByUid(Integer.parseInt(uid));
		final BasicResponse result = new BasicResponse();
		result.status = true;
		result.message = "success";
		List<ArticleDto> articles = articleDao.findAllByUserDto(userOpt.get());

		for (int i = 0; i < articles.size(); ++i) {

			List<ArticleHashtag> list = articleHashtagDao.findAllByArticleDto(articles.get(i));
			ArrayList<HashtagDto> tmpHashtags = new ArrayList<>();
			for (int j = 0; j < list.size(); ++j) {
				tmpHashtags.add(list.get(j).getHashtagDto());
			}

			articles.get(i).setHashtags(tmpHashtags);
			List<ImageDto> tmpImages = imageDao.findAllByArticleDto(articles.get(i));
			ArrayList<String> tmpImagePaths = new ArrayList<>();
			if(tmpImages.size() != 0) {
				for (int j = 0; j < tmpImages.size(); j++) {
					tmpImagePaths.add(tmpImages.get(j).getPostImage());
				}
			}else {
				tmpImagePaths.add("DefaultProfileImage.png");
			}
			articles.get(i).setImagePaths(tmpImagePaths);
			articles.get(i).setUserDto(userOpt.get());

		}
		result.object = articles;
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	// 공개게시글만! -> 다른사람이 메인지도를 볼때!
	@ApiOperation(value = "해당 유저의 모든 공개 게시글을 반환한다", response = List.class)
	@GetMapping("/{uid}/publicArticles")
	public ResponseEntity<BasicResponse> retrievePublicArticles(@PathVariable String uid) {

		Optional<UserDto> userOpt = userDao.findByUid(Integer.parseInt(uid));
		final BasicResponse result = new BasicResponse();
		result.status = true;
		result.message = "success";
		List<ArticleDto> articles = articleDao.findAllByUserDtoAndIsPrivate(userOpt.get(), false);

		for (int i = 0; i < articles.size(); ++i) {

			List<ArticleHashtag> list = articleHashtagDao.findAllByArticleDto(articles.get(i));
			ArrayList<HashtagDto> tmpHashtags = new ArrayList<>();
			for (int j = 0; j < list.size(); ++j) {
				tmpHashtags.add(list.get(j).getHashtagDto());
			}

			articles.get(i).setHashtags(tmpHashtags);
			List<ImageDto> tmpImages = imageDao.findAllByArticleDto(articles.get(i));
			ArrayList<String> tmpImagePaths = new ArrayList<>();
			if(tmpImages.size() != 0) {
				for (int j = 0; j < tmpImages.size(); j++) {
					tmpImagePaths.add(tmpImages.get(j).getPostImage());
				}
			}else {
				tmpImagePaths.add("DefaultArticleImage");
			}
			articles.get(i).setImagePaths(tmpImagePaths);
			articles.get(i).setUserDto(userOpt.get());
		}
		result.object = articles;
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// 비공개게시글만! -> 내 메인지도를 볼때!
	@ApiOperation(value = "해당 유저의 모든 비공개 게시글을 반환한다", response = List.class)
	@GetMapping("/{uid}/privateArticles")
	public ResponseEntity<BasicResponse> retrievePrivateArticles(@PathVariable String uid
//			, HttpServletRequest request
	) {
		final BasicResponse result = new BasicResponse();

		// 요청하는 uid가 본인인지 확인
//		int myUid = jwtService.getUserUid();
//		if(uid != myUid) {
//			result.status = false;
//			result.message = "본인이 아닙니다";
//			return new ResponseEntity<>(result, HttpStatus.OK);
//		}
		System.out.println("before");
		Optional<UserDto> userOpt = userDao.findByUid(Integer.parseInt(uid));
		System.out.println(uid + " " + userOpt.get() + "laksejfoashiefoaisehfoi");
		result.status = true;
		result.message = "success";
		List<ArticleDto> articles = articleDao.findAllByUserDtoAndIsPrivate(userOpt.get(), true);
//		List<ArticleDto> articles = articleDao.findAllByUserDto(userOpt.get());

		for (int i = 0; i < articles.size(); ++i) {

			List<ArticleHashtag> list = articleHashtagDao.findAllByArticleDto(articles.get(i));
			ArrayList<HashtagDto> tmpHashtags = new ArrayList<>();
			for (int j = 0; j < list.size(); ++j) {
				tmpHashtags.add(list.get(j).getHashtagDto());
			}

			articles.get(i).setHashtags(tmpHashtags);
			List<ImageDto> tmpImages = imageDao.findAllByArticleDto(articles.get(i));
			ArrayList<String> tmpImagePaths = new ArrayList<>();
			if(tmpImages.size() != 0) {
				for (int j = 0; j < tmpImages.size(); j++) {
					tmpImagePaths.add(tmpImages.get(j).getPostImage());
				}
			}else {
				tmpImagePaths.add("DefaultArticleImage.png");
			}
			articles.get(i).setImagePaths(tmpImagePaths);
			articles.get(i).setUserDto(userOpt.get());
		}
		result.object = articles;
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/login")
	@ApiOperation(value = "로그인")
	public Object login(@RequestBody @ApiParam(value = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) UserDto user) {
		// @RequestParam(required = true) final String email, @RequestParam(required =
		// true) final String password
		final BasicResponse result = new BasicResponse();

		String email = user.getEmail();
		String password = user.getPassword();
		Optional<UserDto> userOpt = userDao.findUserByEmailAndPassword(email, password);

		if (userOpt.isPresent()) {
			String token = jwtService.create("uid", userOpt.get().getUid(), "access-token");// key, data, subject
			userOpt.get().setLogin(true);
			userDao.save(userOpt.get());
			result.status = true;
			result.message = SUCCESS;
			result.object = token;
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			// resultMap.put("message", "존재하지 않는 사용자입니다.");
			result.status = false;
			result.message = FAIL;
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/account/verify/{key}")
	public ResponseEntity<String> getVerify(@PathVariable String key) {

		try {
			emailService.verifyEmail(key);
			return new ResponseEntity<String>("이메일 인증에 성공하였습니다. 다시 로그인 해주세요.", HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<String>("인증 시간 초과로 인해 이메일 인증에 실패하였습니다.", HttpStatus.OK);
		}

	}

	@PostMapping("/join")
	@ApiOperation(value = "회원가입")
	public Object join(@RequestBody UserDto user) {
		final BasicResponse result = new BasicResponse();
		System.out.println("user: " + user);
		user.setRole("ROLE_USER");
		user.setStateMsg("input stateMsg");
		userDao.save(user);
		result.object = user;
		result.status = true;
		result.message = SUCCESS;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/admin/findAllUser")
	@ApiOperation(value = "모든 회원 반환", response = List.class)
	public Object retrieveUsers() {
		final BasicResponse result = new BasicResponse();
		List<UserDto> list = userDao.findAll();
		result.status = true;
		result.message = SUCCESS;
		result.object = list;
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/findByUsername/{username}")
	@ApiOperation(value = "회원이름으로 찾기")
	public Object retrieveUserByUsername(@PathVariable String username) {
		final BasicResponse result = new BasicResponse();
		Optional<UserDto> findUser = userDao.findByUsername(username);

		if (!findUser.isPresent()) {
			result.status = false;
			result.message = FAIL;
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		} else {
			result.status = true;
			result.message = SUCCESS;
			result.object = findUser;
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	@GetMapping("/findByUid/{uid}")
	@ApiOperation(value = "회원번호로 찾기")
	public Object retrieveUserByUid(@PathVariable String uid) {
		final BasicResponse result = new BasicResponse();
		Optional<UserDto> findUser = userDao.findByUid(Integer.parseInt(uid));
		if (!findUser.isPresent()) {
			result.status = false;
			result.message = FAIL;
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		} else {
			findUser.get().setProfileImagePath(profileImageDao.findByUserDto(findUser.get()).getProfileImage());
			result.status = true;
			result.message = SUCCESS;
			result.object = findUser;
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	@PutMapping
	@ApiOperation(value = "회원정보 수정")
	public Object updateUser(@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestPart("user") UserDto userDto, HttpServletRequest request) {
		System.out.println(file + " " + userDto + " " + request);
		final BasicResponse result = new BasicResponse();
		int uid = jwtService.getUserUid();
		Optional<UserDto> userOpt = userDao.findByUid(uid);
//		System.out.println(userOpt.toString());
		// 회원번호로 검색한 것이 있다면 수정세팅
		if (!userOpt.isPresent()) {
			result.status = false;
			result.message = FAIL;
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		} else {
			// -----프로필 사진 업로드----
			// 파일이 들어왔다면
			if (file != null) {
				UUID uuid = UUID.randomUUID();
				long systemTime = System.currentTimeMillis();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
				String dTime = formatter.format(systemTime);

				String uuidFilename = uuid + dTime + file.getOriginalFilename();

				Path filePath = Paths.get(fileRealPath + "profileImages/" + uuidFilename);

				try {
					Files.write(filePath, file.getBytes());
				} catch (IOException e) {
					result.message = "이미지를 저장하지 못했습니다.";
					result.status = false;
					e.printStackTrace();
					return result;
				}
				System.out.println("hello");
				ProfileImageDto tmpProfileImageDto = profileImageDao.findByUserDto(userDto);
				ProfileImageDto profileImageDto = new ProfileImageDto();
				profileImageDto.setUserDto(userDto);
				profileImageDto.setProfileImage(uuidFilename);
				if(tmpProfileImageDto != null) {
					profileImageDto.setId(tmpProfileImageDto.getId());
				}
				profileImageDao.save(profileImageDto);
				userDto.setProfileImagePath(profileImageDto.getProfileImage());
				// -------------- 이미지 저장 end
			}
			// ---------------------
			userDto.setUid(userOpt.get().getUid());
			userDao.save(userDto);
			result.status = true;
			result.message = SUCCESS;
			result.object = userDto;
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	@DeleteMapping
	@ApiOperation(value = "회원탈퇴")
	public Object deleteUser(HttpServletRequest request) {
		final BasicResponse result = new BasicResponse();
		int uid = jwtService.getUserUid();
		Optional<UserDto> userOpt = userDao.findByUid(uid);

//		System.out.println(userOpt.toString());
		if (!userOpt.isPresent()) {
			result.status = false;
			result.message = FAIL;
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		} else {
			result.status = true;
			result.message = SUCCESS;
			result.object = userOpt.get();
			userDao.delete(userOpt.get());
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

//	@GetMapping("/test")
//	@ApiOperation(value ="회원이름으로 찾기")
//	public void test(HttpServletRequest request){
//		System.out.println(request.getHeader("access-token"));
//		System.out.println(jwtService.getUserEmail());
//	}

}
