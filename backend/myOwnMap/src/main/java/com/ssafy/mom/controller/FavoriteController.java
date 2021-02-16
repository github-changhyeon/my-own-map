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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mom.config.jwt.JwtService;
import com.ssafy.mom.dao.ArticleDao;
import com.ssafy.mom.dao.ArticleHashtagDao;
import com.ssafy.mom.dao.FavoriteDao;
import com.ssafy.mom.dao.ImageDao;
import com.ssafy.mom.dao.UserDao;
import com.ssafy.mom.model.ArticleDto;
import com.ssafy.mom.model.ArticleHashtag;
import com.ssafy.mom.model.BasicResponse;
import com.ssafy.mom.model.HashtagDto;
import com.ssafy.mom.model.ImageDto;
import com.ssafy.mom.model.UserDto;
import com.ssafy.mom.model.UserFavoriteDto;

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
@RequestMapping(value = "/favorite")
@RequiredArgsConstructor
public class FavoriteController {

	@Autowired
	ArticleDao articleDao;

	@Autowired
	UserDao userDao;

	@Autowired
	JwtService jwtService;

	@Autowired
	FavoriteDao favoriteDao;

	@Autowired
	ImageDao imageDao;

	@Autowired
	ArticleHashtagDao articleHashtagDao;

	@GetMapping("/doFavorite/{articleNo}")
	@ApiOperation(value = "찜하기, 찜 취소")
	public Object follow(@PathVariable @ApiParam(value = "찜하기 할시 필요한 게시글No, jwt", required = true) int articleNo,
			HttpServletRequest request) {
		final BasicResponse result = new BasicResponse();
		int myUid = jwtService.getUserUid();
		Optional<UserDto> user = userDao.findByUid(myUid);
		Optional<ArticleDto> article = articleDao.findByArticleNo(articleNo);
		Optional<UserFavoriteDto> isFavorite = favoriteDao.findByUserDtoAndArticleDto(user.get(), article.get());
		if (isFavorite.isPresent()) {
			result.status = true;
			result.message = "찜취소.";
			favoriteDao.deleteById(isFavorite.get().getFavoriteNo());
			result.object = retrieveFavorite(request);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			result.status = true;
			result.message = "찜하기.";
			UserFavoriteDto favoriteEntity = UserFavoriteDto.builder().userDto(user.get()).articleDto(article.get())
					.build();
			favoriteDao.save(favoriteEntity);
			result.object = retrieveFavorite(request);
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	// 현재 찜되잇는지확인하기위한 상태체크
	@GetMapping("/isFavorite/{articleNo}")
	@ApiOperation(value = "jwt가 해당게시글을 찜하고있는지 체크, 반환값 true, false")
	public Object isFollow(
			@PathVariable @ApiParam(value = "찜하고 있는치 체크할 때  필요한 게시글No, jwt", required = true) int articleNo,
			HttpServletRequest request) {
		final BasicResponse result = new BasicResponse();
		int myUid = jwtService.getUserUid();
		Optional<UserDto> user = userDao.findByUid(myUid);
		Optional<ArticleDto> article = articleDao.findByArticleNo(articleNo);
		Optional<UserFavoriteDto> isFavorite = favoriteDao.findByUserDtoAndArticleDto(user.get(), article.get());
		if (isFavorite.isPresent()) {
			result.status = true;
			result.message = "찜하는 중입니다.";
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			result.status = false;
			result.message = "찜하는 중이 아닙니다.";
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	// 멤버가 찜한 목록 보기
	@GetMapping("/myFavorite")
	@ApiOperation(value = "uid가 찜하고 있는 게시글 보기")
	public Object retrieveFavorite(
			@ApiParam(value = "찜목록 가져올때   필요한  jwt", required = true) HttpServletRequest request) {
		final BasicResponse result = new BasicResponse();
		int uid = jwtService.getUserUid();
		Optional<UserDto> userEntity = userDao.findByUid(uid);
		List<UserFavoriteDto> FavoriteEntities = favoriteDao.findAllByUserDto(userEntity.get());
		if (FavoriteEntities.size() == 0) {
			result.status = false;
			result.message = "찜한 게시글이 없습니다!";
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		List<ArticleDto> articleEntites = new ArrayList<ArticleDto>();
		for (int i = 0; i < FavoriteEntities.size(); i++) {
			List<ArticleHashtag> list = articleHashtagDao.findAllByArticleDto(FavoriteEntities.get(i).getArticleDto());
			ArrayList<HashtagDto> tmpHashtags = new ArrayList<>();
			for (int j = 0; j < list.size(); ++j) {
				tmpHashtags.add(list.get(j).getHashtagDto());
			}

			FavoriteEntities.get(i).getArticleDto().setHashtags(tmpHashtags);
			List<ImageDto> tmpImages = imageDao.findAllByArticleDto(FavoriteEntities.get(i).getArticleDto());
			ArrayList<String> tmpImagePaths = new ArrayList<>();
			if (tmpImages.size() != 0) {
				for (int j = 0; j < tmpImages.size(); j++) {
					tmpImagePaths.add(tmpImages.get(j).getPostImage());
				}
			}else {
				tmpImagePaths.add("DefaultArticleImgae.png");
			}
			FavoriteEntities.get(i).getArticleDto().setImagePaths(tmpImagePaths);
			articleEntites.add(FavoriteEntities.get(i).getArticleDto());
		}
		result.status = true;
		result.message = "찜한 게시글이 있습니다";
		result.object = articleEntites;
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
