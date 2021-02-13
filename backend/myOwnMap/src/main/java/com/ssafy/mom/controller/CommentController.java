package com.ssafy.mom.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;


import com.ssafy.mom.config.jwt.JwtService;
import com.ssafy.mom.dao.ArticleDao;
import com.ssafy.mom.dao.CommentDao;
import com.ssafy.mom.dao.UserDao;
import com.ssafy.mom.model.ArticleDto;
import com.ssafy.mom.model.BasicResponse;
import com.ssafy.mom.model.CommentDto;
import com.ssafy.mom.model.UserDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

//@CrossOrigin(origins = { "http://localhost:3000" })
@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping(value = "/comments")
public class CommentController {

	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Autowired
	UserDao userDao;

	@Autowired
	ArticleDao articleDao;

	@Autowired
	CommentDao commentDao;

	@Autowired
	JwtService jwtService;

	// create
	@PostMapping
	@ApiOperation(value = "댓글 등록")
	public Object createComment(
			@RequestBody @ApiParam(value = "댓글 작성시 필요한 정보(댓글내용, 유저id, 게시글no).", required = true) Map<String, String> dtoMap) {
		final BasicResponse result = new BasicResponse();
		int uid =Integer.parseInt(dtoMap.get("uid"));
		System.out.println(uid + "uid");
		
		int articleNo = Integer.parseInt(dtoMap.get("articleNo"));
		System.out.println(articleNo + "articleNo");
		Optional<ArticleDto> article = articleDao.findByArticleNo(articleNo);
		Optional<UserDto> user = userDao.findByUid(uid);
		CommentDto comment = new CommentDto();
		comment.setArticleDto(article.get());
		comment.setUserDto(user.get());
		comment.setContent(dtoMap.get("content"));
		CommentDto commentEntity = commentDao.save(comment);
		if (commentEntity != null) {
			result.message = "댓글 등록 성공";
			result.status = true;
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			result.message = "댓글 등록 실패";
			result.status = false;
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

		}
	}

	// retrieve
	@GetMapping("/{articleNo}")
	@ApiOperation(value = "해당 게시글의 모든 댓글 반환")
	public Object retrieveComment(@PathVariable @ApiParam(value = "댓글 정보 반환 시 필요한 정보(게시글 번호)") int articleNo) {
		final BasicResponse result = new BasicResponse();
		Optional<ArticleDto> articleEntity = articleDao.findByArticleNo(articleNo);
		// 해당 게시글이 없다면
		if (!articleEntity.isPresent()) {
			result.message = "해당 게시글이 없습니다.";
			result.status = false;
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
		// 있다면 댓글 찾기
		List<CommentDto> commentEntities = commentDao.findAllByArticleDto(articleEntity.get());

		// 댓글이 한개두 없다면
		if (commentEntities.size() == 0) {
			result.message = "댓글이 없습니다";
			result.status = true;
			result.object = new ArrayList<CommentDto> ();
			return new ResponseEntity<>(result, HttpStatus.OK);
		} // 있다면
		else {
			result.message = "댓글 불러오기 성공";
			result.status = true;
			result.object = commentEntities;
			return new ResponseEntity<>(result, HttpStatus.OK);

		}

	}

	// update
	@PutMapping
	@ApiOperation(value = "해당 게시글 수정")
	public Object updateComment(
			@RequestBody @ApiParam(value = "댓글 수정 시 필요한 정보(댓글번호 ,댓글내용,jwt).", required = true) CommentDto comment
			,HttpServletRequest request
			) {
		final BasicResponse result = new BasicResponse();
		Optional<CommentDto> commentEntity = commentDao.findById(comment.getCommentNo());
		int myUid = jwtService.getUserUid();

		// 댓글 작성자가 아니면 수정x
		if (commentEntity.get().getUserDto().getUid() != myUid) {
			result.message = "댓글 작성자가 아니면 수정할 수 없습니다.";
			result.status = false;
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
		// 댓글이 없다면 false 반환
		if (!commentEntity.isPresent()) {
			result.message = "해당 댓글이 없습니다.";
			result.status = false;
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}

		// 댓글이 있다면 수정
		commentEntity.get().setContent(comment.getContent());
		commentDao.save(commentEntity.get());
		result.message = "댓글 불러오기 성공";
		result.status = true;
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// delete
	@DeleteMapping("/{commentNo}")
	@ApiOperation(value = "해당 댓글 삭제")
	public Object deleteComment(@PathVariable @ApiParam(value = "댓글 삭제 시 필요한 정보(댓글번호, jwt) ") int commentNo
			,HttpServletRequest request
			) {
		final BasicResponse result = new BasicResponse();
		Optional<CommentDto> commentEntity = commentDao.findById(commentNo);
		int myUid = jwtService.getUserUid();
		// 댓글 작성자가 아니면 수정x
		if (commentEntity.get().getUserDto().getUid() != myUid) {
			result.message = "댓글 작성자가 아니면 삭제할 수 없습니다.";
			result.status = false;
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
		// 댓글이 없다면 false 반환
		if (!commentEntity.isPresent()) {
			result.message = "해당 댓글이 없습니다.";
			result.status = false;
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
		//있다면 삭제
		commentDao.delete(commentEntity.get());
		result.message = "댓글 삭제 성공";
		result.status = true;
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
