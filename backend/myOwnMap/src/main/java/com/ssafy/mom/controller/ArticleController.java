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

	@GetMapping
	@ApiOperation(value = "전체 게시물을 받아온다", response = List.class)
	public Object retrieveAllArticles() {
		List<ArticleDto> list = articleDao.findAll();
		for (int i = 0; i < list.size(); i++) {
			ArticleDto articleDto = list.get(i);
			List<ImageDto> tmpImages = imageDao.findAllByArticleDto(articleDto);
			ArrayList<String> tmpImagePaths = new ArrayList<>();
			for (int j = 0; j < tmpImages.size(); j++) {
				tmpImagePaths.add(tmpImages.get(j).getPostImage());
			}
			articleDto.setImagePaths(tmpImagePaths);
		}
		final BasicResponse result = new BasicResponse();
		result.status = true;
		result.message = "전체 게시글 반환에 성공하였습니다.";
		result.object = list;
		return result;
	}

	@PostMapping
	@ApiOperation(value = "게시글 등록한다", response = List.class)
	public Object createArticle(@RequestPart(value = "file[]", required = false) List<MultipartFile> file,
			@RequestPart("article") ArticleDto articleDto) {
		final BasicResponse result = new BasicResponse();
//		System.out.println(file);
		System.out.println(articleDto);
		// 이미지 업로드 수행
		if (file != null) {
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

				Path filePath = Paths.get(fileRealPath + "upload/" + uuidFilename);

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
		}
		// -------------- 이미지 저장 end

		// TODO: 회원정보 연동시 uid 가져오기
		int uid = articleDto.getUserDto().getUid();

		Optional<UserDto> userOpt = userDao.findByUid(uid);

		if (!userOpt.isPresent()) {
			result.status = false;
			result.message = "유저 정보가 없습니다.";
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		articleDto.setUserDto(userOpt.get());
		// 게시물 + 이미지 자체는 저장완료
		articleDao.save(articleDto);

		// article-hashtag-user hashtag묶음
		// 모든 해쉬태그 정보 반환
		List<HashtagDto> hashtags = hashtagDao.findAll();
		// 현재 작성한 글의 해쉬태그 반환
		List<HashtagDto> inputHashtags = articleDto.getHashtags();
		
		// 중복 제거를 위한 hashtagSet
		Set<String> hashtagSet = new TreeSet<String>();
		for (int i = 0; i < hashtags.size(); i++) {
			hashtagSet.add(hashtags.get(i).getHashtagName());
		}
		System.out.println(articleDto.isPrivate()+ "let me see" + " " + inputHashtags.size());
		// 작성된  글의 해쉬태그를 돌면서
		for (int i = 0; i < inputHashtags.size(); i++) {
			// 이미 존재하는 해쉬태그라면, 해당 해쉬태그 정보를 가져온다.
			if (hashtagSet.contains(inputHashtags.get(i).getHashtagName())) {
				HashtagDto alreadyExist = hashtagDao.findByHashtagName(inputHashtags.get(i).getHashtagName());
				// 혹시 비공개 글이라면
				if (articleDto.isPrivate()) {
					// userHashtag에 정보가 없다면
					System.out.println("private ha?");
					if(!userHashtagDao.findByUserDtoAndHashtagDto(userOpt.get(), alreadyExist).isPresent()) {
						userHashtagDao.save(new UserHashtag(userOpt.get(), alreadyExist, 0));
					// userHashtag에 정보가 있다면
					}else {
						// 아무것도 안해줌 ㅇㅇ
					}
				// 공개 글이라면
				}else {

					System.out.println("public ha?");
					// userHashtag에 정보가 없다면
					if(!userHashtagDao.findByUserDtoAndHashtagDto(userOpt.get(), alreadyExist).isPresent()) {
						userHashtagDao.save(new UserHashtag(userOpt.get(), alreadyExist, 1));
					// userHashtag에 정보가 있다면 public
					}else {
						Optional<UserHashtag> userHashtagDto = userHashtagDao.findByUserDtoAndHashtagDto(userOpt.get(), alreadyExist);
//						userHashtagDto.get().setHashtagDto(userHashtagDto.get().getHashtagDto());
//						userHashtagDto.get().setId(userHashtagDto.get().getId());
						userHashtagDto.get().setPublicCnt(userHashtagDto.get().getPublicCnt()+1);
//						userHashtagDto.get().setUserDto(userHashtagDto.get().getUserDto());
						userHashtagDao.save(userHashtagDto.get());
					}
					
				}
				articleHashtagDao.save(new ArticleHashtag(articleDto, alreadyExist));
			// 존재하지 않는 해쉬태그라면, 해쉬태그를 저장하고 시작한다
			}else {
				hashtagDao.save(inputHashtags.get(i));
				// userHashtag를 저장하는데, 비공개글이라면 isPublic = 0, 공개글이라면 isPublic = 1
				userHashtagDao.save(new UserHashtag(userOpt.get(), inputHashtags.get(i),articleDto.isPrivate()?0:1));		
				
				articleHashtagDao.save(new ArticleHashtag(articleDto, inputHashtags.get(i)));
				
			}
		}
		
		result.status = true;
		result.message = "success";
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	@ApiOperation(value = "게시글 번호에 해당하는 게시글을 반환한다", response = List.class)
	@GetMapping("/{articleNo}")
	public Object retrieveArticleByArticleNo(@PathVariable String articleNo) {

		// TODO: 회원정보 연동시 uid 가져오기
		// int uid = 1;

		Optional<ArticleDto> articleOpt = articleDao.findByArticleNo(Integer.parseInt(articleNo));
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

		final BasicResponse result = new BasicResponse();
		
		result.status = true;
		result.message = "success";
		result.object = articleOpt.get();

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@ApiOperation(value = "게시글 수정 후 성공/실패 여부를 반환한다", response = List.class)
	@PostMapping("/{articleNo}")
	public Object updateArticle(@RequestBody ArticleDto articleDto, @PathVariable String articleNo) {
		System.out.println("해쉬태그임" + articleDto.getHashtags());
		final BasicResponse result = new BasicResponse();
		
		// TODO: 회원정보 연동시 uid 가져오기
		int uid = articleDto.getUserDto().getUid();

		// 요청하는 uid가 본인인지 확인
//		int myUid = jwtService.getUserUid();
//		if(uid != myUid) {
//			result.status = false;
//			result.message = "본인이 아닙니다";
//			return new ResponseEntity<>(result, HttpStatus.OK);
//		}

		// 게시글번호로 게시글 찾기
		Optional<ArticleDto> articleOpt = articleDao.findByArticleNo(Integer.parseInt(articleNo));
		// 없다면 없다고 반환
		if (!articleOpt.isPresent())
			return new ResponseEntity<String>("article not found", HttpStatus.BAD_REQUEST);
		// 작성자의 유저정보 가져오기
		Optional<UserDto> userOpt = userDao.findByUid(uid);
		// 유저 정보는 이미 들어있음
//		articleDto.setUserDto(userOpt.get());
		// TODO: Front에서 ArticleNo 돌려줄때 지워주기
		articleDto.setArticleNo(Integer.parseInt(articleNo));
		articleDao.save(articleDto);

		// --- 게시물 수정 끝
		// private이라면
		if (articleDto.isPrivate()) {
			// 게시글에 관련된 모든 게시글-해쉬태그 정보를 가져온다.
			List<ArticleHashtag> alreadyArticleHashtags = articleHashtagDao.findAllByArticleDto(articleOpt.get());
//		System.out.println("진짜해쉬태그" + alreadyArticleHashtags);
//		System.out.println("해쉬태그는" + articleOpt.get().getHashtags());

			// 해당 게시글의 해쉬태그가 존재하지 않는다면
			if (alreadyArticleHashtags == null)
				// nullPointer방지를 위해서 일단 정의
				alreadyArticleHashtags = new ArrayList<>();
			// 해당 게시글의 해쉬태그를 순회하면서
			for (int i = 0; i < alreadyArticleHashtags.size(); ++i) {
				int cnt = userHashtagDao.countByHashtagDto(alreadyArticleHashtags.get(i).getHashtagDto());
				// 혹시 비공개 게시글의 해당 해쉬태그가 유저가 게시물에 붙였던 유일한 해쉬태그라면
				// 지워준다
				if (cnt == 1)
					userHashtagDao.deleteByHashtagDto(alreadyArticleHashtags.get(i).getHashtagDto());
			}
		} else {
			// 게시글에 관련된 모든 게시글-해쉬태그 정보를 가져온다.
			List<ArticleHashtag> alreadyArticleHashtags = articleHashtagDao.findAllByArticleDto(articleOpt.get());
//		System.out.println("진짜해쉬태그" + alreadyArticleHashtags);
//		System.out.println("해쉬태그는" + articleOpt.get().getHashtags());

			// 해당 게시글의 해쉬태그가 존재하지 않는다면
			if (alreadyArticleHashtags == null)
				// nullPointer방지를 위해서 일단 정의
				alreadyArticleHashtags = new ArrayList<>();
			// 해당 게시글의 해쉬태그를 순회하면서
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
						userHashtagDao.save(new UserHashtag(userOpt.get(), alreadyExist,0));
					}
					;
					articleHashtagDao.save(new ArticleHashtag(articleDto, alreadyExist));
					continue;
				}
				hashtagDao.save(inputHashtags.get(i));
				if (!userHashtagDao.findByUserDtoAndHashtagDto(userOpt.get(), inputHashtags.get(i)).isPresent()) {
					userHashtagDao.save(new UserHashtag(userOpt.get(), inputHashtags.get(i),1));
				}
				;
				articleHashtagDao.save(new ArticleHashtag(articleDto, inputHashtags.get(i)));
			}
			
			result.status = true;
			result.message = "success";
			result.object = articleOpt.get();
			return new ResponseEntity<>(result, HttpStatus.OK);

		}
		result.status = true;
		result.message = "success";
		result.object = articleOpt.get();
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	@ApiOperation(value = "게시글 삭제 후 성공/실패 여부를 반환한다", response = List.class)
	@DeleteMapping("/{articleNo}")
	public ResponseEntity<String> deleteArticle(@PathVariable String articleNo) {

		// TODO: 회원정보 연동시 uid 가져오기
//		 int uid = 1;

		// 게시글 번호로 게시글dto 검색
		Optional<ArticleDto> articleOpt = articleDao.findByArticleNo(Integer.parseInt(articleNo));
		// 없는 게시물이라면 fail
		if (!articleOpt.isPresent())
			return new ResponseEntity<String>("fail", HttpStatus.OK);
		// 게시물에 해당하는 게시물-해쉬태그값을 가져온다 그 묶음이
		List<ArticleHashtag> alreadyArticleHashtags = articleHashtagDao.findAllByArticleDto(articleOpt.get());
		System.out.println("진짜해쉬태그" + alreadyArticleHashtags);
		System.out.println("해쉬태그는" + articleOpt.get().getHashtags());

		if (alreadyArticleHashtags == null)
			alreadyArticleHashtags = new ArrayList<>();
		// 이미 가지고 있는 해쉬태그를 순회
		for (int i = 0; i < alreadyArticleHashtags.size(); ++i) {
			// 그중 하나 해쉬태그로 유저-해쉬태그를 검색
//			int cnt = userHashtagDao.countByHashtagDto(alreadyArticleHashtags.get(i).getHashtagDto());
//			UserHashtag userHashtag = userHashtagDao.findAll(alreadyArticleHashtags.get(i).getHashtagDto().getHashtagNo());
//			List<UserHashtag> tmpUserHashtags = userHashtagDao.findAllByHashtagNo(alreadyArticleHashtags.get(i).getHashtagDto().getHashtagNo()); 
			int cnt = (int) articleHashtagDao
					.findAllByHashtagNo(alreadyArticleHashtags.get(i).getHashtagDto().getHashtagNo());
			System.out.println("I found " + cnt);
//			System.out.println(alreadyArticleHashtags.get(i).getHashtagDto() + "카운트는" + tmpUserHashtags.size() + "찾은건");

			UserHashtag userHashtag = userHashtagDao.findByHashtagDto(alreadyArticleHashtags.get(i).getHashtagDto());
			// 혹시 해당 해쉬태그로 검색한 게시글이 하나면 지우기 
			if (cnt == 1) {
				userHashtagDao.deleteByHashtagDto(alreadyArticleHashtags.get(i).getHashtagDto());
			// 게시글은 여러개지만, 현재 지우는 게시물이 public한 게시물이라면, 해당 userHashtag관계의 publicCnt를 1 감소시킨다
			}else if(!articleOpt.get().isPrivate()) {
				userHashtag.setPublicCnt(userHashtag.getPublicCnt() - 1);
			}
		}

		imageDao.deleteAllByArticleDto(articleOpt.get());

		articleHashtagDao.deleteByArticleDto(articleOpt.get());
		articleDao.delete(articleOpt.get());
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

}