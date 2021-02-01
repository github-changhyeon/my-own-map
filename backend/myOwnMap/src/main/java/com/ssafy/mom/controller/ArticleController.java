package com.ssafy.mom.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mom.dao.ArticleDao;
import com.ssafy.mom.dao.ArticleHashtagDao;
import com.ssafy.mom.dao.HashtagDao;
import com.ssafy.mom.dao.UserDao;
import com.ssafy.mom.dao.UserHashtagDao;
import com.ssafy.mom.model.ArticleDto;
import com.ssafy.mom.model.ArticleHashtag;
import com.ssafy.mom.model.BasicResponse;
import com.ssafy.mom.model.HashtagDto;
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
	public Object createArticle(@RequestBody ArticleDto articleDto) {

		// TODO: 회원정보 연동시 uid 가져오기
		int uid = 1;

		final BasicResponse result = new BasicResponse();
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

	@ApiOperation(value = "해당 유저의 모든 게시글을 반환한다", response = List.class)
	@GetMapping
	public ResponseEntity<BasicResponse> retrieveArticles() {

		// TODO: 회원정보 연동시 uid 가져오기
		int uid = 1;

		Optional<UserDto> userOpt = userDao.findByUid(uid);
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

		}
		result.object = articles;
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
		return articleOpt;
	}

	@ApiOperation(value = "해당 유저의 해쉬태그를 모두 반환한다.", response = List.class)
	@GetMapping("/hashtags")
	public ResponseEntity<BasicResponse> retrieveHashtags() {

		// TODO: 회원정보 연동시 uid 가져오기
		int uid = 1;

		Optional<UserDto> userOpt = userDao.findByUid(uid);
		List<UserHashtag> list = userHashtagDao.findAllByUserDto(userOpt.get());

		List<HashtagDto> hashtags = new ArrayList<HashtagDto>();
		for (int i = 0; i < list.size(); i++) {
			hashtags.add(list.get(i).getHashtagDto());
		}

//		System.out.println(hashtags);
		final BasicResponse result = new BasicResponse();
		result.status = true;
		result.message = "success";
		result.object = hashtags;
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@ApiOperation(value = "해당 유저의 최신 게시물 10개를 받아온다", response = List.class)
	@GetMapping("/recent")
	public ResponseEntity<BasicResponse> retrieveNewTenArticle() {

		// TODO: 회원정보 연동시 uid 가져오기
		int uid = 1;

		Optional<UserDto> userOpt = userDao.findByUid(uid);
		// TODO: 작성 시간이 확립되면 진행
		List<ArticleDto> articles = articleDao.findTop10ByUserDtoOrderByUpdateTimeDesc(userOpt.get());
		for(int i = 0; i < articles.size(); ++i) {
			List<ArticleHashtag> articleHashtags = articleHashtagDao.findAllByArticleDto(articles.get(i));
			ArrayList<HashtagDto> tmpHashtags = new ArrayList<>();
			for (int j = 0; j < articleHashtags.size(); ++j) {
				tmpHashtags.add(articleHashtags.get(j).getHashtagDto());
			}

			articles.get(i).setHashtags(tmpHashtags);
		}
		
		final BasicResponse result = new BasicResponse();
		result.status = true;
		result.message = "success";
		result.object = articles;
//		System.out.println(list);
		return new ResponseEntity<>(result, HttpStatus.OK);
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
		
//		System.out.println(articleOpt.get());
		List<ArticleHashtag> alreadyArticleHashtags = articleHashtagDao.findAllByArticleDto(articleOpt.get());
//		System.out.println("진짜해쉬태그" + alreadyArticleHashtags);
//		System.out.println("해쉬태그는" + articleOpt.get().getHashtags());
		if(alreadyArticleHashtags == null)
			alreadyArticleHashtags = new ArrayList<>();
		for(int i = 0; i < alreadyArticleHashtags.size(); ++i) {
			int cnt = userHashtagDao.countByHashtagDto(alreadyArticleHashtags.get(i).getHashtagDto());
//			System.out.println("카운트는" + cnt);
			if(cnt == 1)
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
		if(alreadyArticleHashtags == null)
			alreadyArticleHashtags = new ArrayList<>();
		for(int i = 0; i < alreadyArticleHashtags.size(); ++i) {
			int cnt = userHashtagDao.countByHashtagDto(alreadyArticleHashtags.get(i).getHashtagDto());
//			System.out.println("카운트는" + cnt);
			if(cnt == 1)
				userHashtagDao.deleteByHashtagDto(alreadyArticleHashtags.get(i).getHashtagDto());
		}
		
		articleHashtagDao.deleteByArticleDto(articleOpt.get());
		articleDao.delete(articleOpt.get());
//		userHashtagDao.findByUserDtoAndHashtagDto(userDto, alreadyExist)
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
}