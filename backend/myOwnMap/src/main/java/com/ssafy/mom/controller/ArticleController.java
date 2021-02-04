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
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.mom.dao.ArticleDao;
import com.ssafy.mom.dao.ArticleHashtagDao;
import com.ssafy.mom.dao.HashtagDao;
import com.ssafy.mom.dao.ImageDao;
import com.ssafy.mom.dao.UserDao;
import com.ssafy.mom.dao.UserHashtagDao;
import com.ssafy.mom.model.ArticleDto;
import com.ssafy.mom.model.ArticleHashtag;
import com.ssafy.mom.model.BasicResponse;
import com.ssafy.mom.model.HashtagDto;
import com.ssafy.mom.model.ImageDto;
import com.ssafy.mom.model.UserDto;
import com.ssafy.mom.model.UserHashtag;
import com.sun.istack.Nullable;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

//@CrossOrigin(origins = { "http://localhost:3000" })
@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping(value = "/articles")
public class ArticleController {

	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Value("${file.path}")
	private String fileRealPath;

	@Autowired
	ImageDao imageDao;

	@Autowired
	ArticleDao articleDao;

	@Autowired
	UserDao userDao;

	@Autowired
	HashtagDao hashtagDao;

	@Autowired
	UserHashtagDao userHashtagDao;

	@Autowired
	ArticleHashtagDao articleHashtagDao;

	@PostMapping
	@ApiOperation(value = "게시글 등록한다")
	public Object createArticle(@RequestPart(value = "file[]", required = false) List<MultipartFile> file,
			@RequestPart("article") ArticleDto articleDto) {
		final BasicResponse result = new BasicResponse();
//		System.out.println(file);
		System.out.println(articleDto);
		// 이미지 업로드 수행
		if(file != null) {
			UUID uuid = UUID.randomUUID();
			System.out.println(file.size());
			int saveCnt = 0;
			while (saveCnt < file.size()) {
				// 현재 시스템 시간 구하기
				long systemTime = System.currentTimeMillis();
				// 출력 형태를 위한 formmater
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
				// format에 맞게 출력하기 위한 문자열 변환
				String dTime = formatter.format(systemTime);
				
				String uuidFilename = uuid + "_" + dTime + file.get(saveCnt).getOriginalFilename();
				
				Path filePath = Paths.get(fileRealPath + uuidFilename);
				
				try {
					Files.write(filePath, file.get(saveCnt).getBytes()); // 하드디스크 기록
				} catch (IOException e) {
					result.message = "이미지를 저장하지 못했습니다.";
					result.status = false;
					e.printStackTrace();
					return result;
				}
				
				ImageDto imageDto = new ImageDto();
				imageDto.setArticleDto(articleDto);
				imageDto.setPostImage(uuidFilename);
				
				imageDao.save(imageDto);
				saveCnt++;
			}
			// -------------- 이미지 저장 end
		}

		// TODO: 회원정보 연동시 uid 가져오기
//		int uid = 1;
		int uid = articleDto.getUid();

		Optional<UserDto> userOpt = userDao.findByUid(uid);

		if (!userOpt.isPresent()) {
			result.status = false;
			result.message = "유저 정보가 없습니다.";
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

		// TODO : 게시물 작성 시간 등록
		articleDto.setUserDto(userOpt.get());
		articleDao.save(articleDto);

		// hash - user
		// 모든 해쉬태그 정보 반환
		List<HashtagDto> hashtags = hashtagDao.findAll();
		// 현재 작성한 글의 해쉬태그 반환
		List<HashtagDto> inputHashtags = articleDto.getHashtags();

		Set<String> hashtagSet = new TreeSet<String>();

		for (int i = 0; i < hashtags.size(); i++) {
			hashtagSet.add(hashtags.get(i).getHashtagName());
		}

		for (int i = 0; i < inputHashtags.size(); i++) {
			if (hashtagSet.contains(inputHashtags.get(i).getHashtagName())) {
				// 이미 해당 해쉬태그가 존재한다면, 해당 해쉬태그의 번호를 조회한다.
				HashtagDto alreadyExist = hashtagDao.findByHashtagName(inputHashtags.get(i).getHashtagName());
				if (!userHashtagDao.findByUserDtoAndHashtagDto(userOpt.get(), alreadyExist).isPresent()) {
					userHashtagDao.save(new UserHashtag(userOpt.get(), alreadyExist));
				}
				;
				articleHashtagDao.save(new ArticleHashtag(articleDto, alreadyExist));
				continue;
			}
			hashtagDao.save(inputHashtags.get(i));
			if (!userHashtagDao.findByUserDtoAndHashtagDto(userOpt.get(), inputHashtags.get(i)).isPresent()) {
				userHashtagDao.save(new UserHashtag(userOpt.get(), inputHashtags.get(i)));
			}
			;
			articleHashtagDao.save(new ArticleHashtag(articleDto, inputHashtags.get(i)));
		}

		result.status = true;
		result.message = "success";
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	@ApiOperation(value = "게시글 번호에 해당하는 게시글을 반환한다", response = List.class)
	@GetMapping("/{articleNo}")
	public Optional<ArticleDto> retrieveArticleByArticleNo(@PathVariable int articleNo) {

		// TODO: 회원정보 연동시 uid 가져오기
		// int uid = 1;

		Optional<ArticleDto> articleOpt = articleDao.findByArticleNo(articleNo);
		List<ArticleHashtag> list = articleHashtagDao.findAllByArticleDto(articleOpt.get());
		ArrayList<HashtagDto> tmpHashtags = new ArrayList<>();
		for (int j = 0; j < list.size(); ++j) {
			tmpHashtags.add(list.get(j).getHashtagDto());
		}

		articleOpt.get().setHashtags(tmpHashtags);
		List<ImageDto> tmpImages = imageDao.findAllByArticleDto(articleOpt.get());
		ArrayList<String> tmpImagePaths = new ArrayList<>();
		for (int i = 0; i < tmpImages.size(); i++) {
			tmpImagePaths.add(tmpImages.get(i).getPostImage());
		}
		articleOpt.get().setImagePaths(tmpImagePaths);
		return articleOpt;
	}

	@ApiOperation(value = "게시글 수정 후 성공/실패 여부를 반환한다", response = List.class)
	@PostMapping("/{articleNo}")
	public ResponseEntity<String> updateArticle(@RequestBody ArticleDto articleDto, @PathVariable int articleNo) {

		// TODO: 회원정보 연동시 uid 가져오기
		int uid = 1;

		Optional<ArticleDto> articleOpt = articleDao.findByArticleNo(articleNo);
		if (!articleOpt.isPresent())
			return new ResponseEntity<String>("article not found", HttpStatus.BAD_REQUEST);
		Optional<UserDto> userOpt = userDao.findByUid(uid);
		articleDto.setUserDto(userOpt.get());
		articleDto.setArticleNo(articleNo);
		articleDao.save(articleDto);

		// 기존 article hashtag 삭제하기
		// 기존 user hashtag 삭제하기

		List<ArticleHashtag> alreadyArticleHashtags = articleHashtagDao.findAllByArticleDto(articleOpt.get());
//		System.out.println("진짜해쉬태그" + alreadyArticleHashtags);
//		System.out.println("해쉬태그는" + articleOpt.get().getHashtags());
		if (alreadyArticleHashtags == null)
			alreadyArticleHashtags = new ArrayList<>();
		for (int i = 0; i < alreadyArticleHashtags.size(); ++i) {
			int cnt = userHashtagDao.countByHashtagDto(alreadyArticleHashtags.get(i).getHashtagDto());
//			System.out.println("카운트는" + cnt);
			if (cnt == 1)
				userHashtagDao.deleteByHashtagDto(alreadyArticleHashtags.get(i).getHashtagDto());
		}
		articleHashtagDao.deleteByArticleDto(articleDto);

		List<HashtagDto> hashtags = hashtagDao.findAll();
		// 현재 작성한 글의 해쉬태그 반환
		List<HashtagDto> inputHashtags = articleDto.getHashtags();

		Set<String> hashtagSet = new TreeSet<String>();

		for (int i = 0; i < hashtags.size(); i++) {
			hashtagSet.add(hashtags.get(i).getHashtagName());
		}

		for (int i = 0; i < inputHashtags.size(); i++) {
			if (hashtagSet.contains(inputHashtags.get(i).getHashtagName())) {
				// 이미 해당 해쉬태그가 존재한다면, 해당 해쉬태그의 번호를 조회한다.
				HashtagDto alreadyExist = hashtagDao.findByHashtagName(inputHashtags.get(i).getHashtagName());
				if (!userHashtagDao.findByUserDtoAndHashtagDto(userOpt.get(), alreadyExist).isPresent()) {
					userHashtagDao.save(new UserHashtag(userOpt.get(), alreadyExist));
				}
				;
				articleHashtagDao.save(new ArticleHashtag(articleDto, alreadyExist));
				continue;
			}
			hashtagDao.save(inputHashtags.get(i));
			if (!userHashtagDao.findByUserDtoAndHashtagDto(userOpt.get(), inputHashtags.get(i)).isPresent()) {
				userHashtagDao.save(new UserHashtag(userOpt.get(), inputHashtags.get(i)));
			}
			;
			articleHashtagDao.save(new ArticleHashtag(articleDto, inputHashtags.get(i)));
		}

		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@ApiOperation(value = "게시글 삭제 후 성공/실패 여부를 반환한다", response = List.class)
	@DeleteMapping("/{articleNo}")
	public ResponseEntity<String> deleteArticle(@PathVariable int articleNo) {

		// TODO: 회원정보 연동시 uid 가져오기
//		 int uid = 1;

		Optional<ArticleDto> articleOpt = articleDao.findByArticleNo(articleNo);
		if (!articleOpt.isPresent())
			return new ResponseEntity<String>("fail", HttpStatus.OK);

		List<ArticleHashtag> alreadyArticleHashtags = articleHashtagDao.findAllByArticleDto(articleOpt.get());
//		System.out.println("진짜해쉬태그" + alreadyArticleHashtags);
//		System.out.println("해쉬태그는" + articleOpt.get().getHashtags());
		if (alreadyArticleHashtags == null)
			alreadyArticleHashtags = new ArrayList<>();
		for (int i = 0; i < alreadyArticleHashtags.size(); ++i) {
			int cnt = userHashtagDao.countByHashtagDto(alreadyArticleHashtags.get(i).getHashtagDto());
//			System.out.println("카운트는" + cnt);
			if (cnt == 1)
				userHashtagDao.deleteByHashtagDto(alreadyArticleHashtags.get(i).getHashtagDto());
		}

		imageDao.deleteAllByArticleDto(articleOpt.get());

		articleHashtagDao.deleteByArticleDto(articleOpt.get());
		articleDao.delete(articleOpt.get());
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

}