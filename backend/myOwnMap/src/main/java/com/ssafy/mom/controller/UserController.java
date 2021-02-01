package com.ssafy.mom.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestMapping;


import com.ssafy.mom.config.jwt.JwtService;
import com.ssafy.mom.config.jwt.JwtServiceImpl;
import com.ssafy.mom.dao.UserDao;
import com.ssafy.mom.email.EmailService;
import com.ssafy.mom.model.BasicResponse;
import com.ssafy.mom.model.UserDto;


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
@RequestMapping(value="/users")
@RequiredArgsConstructor
public class UserController {
	
	private static final String SUCCESS = "success";
	private static final String FAIL ="fail";

	
	@Autowired
	private UserDao userDao;


	@Autowired
	EmailService emailService;
	

	@Autowired
	private JwtServiceImpl jwtService;
	
	@PostMapping("/login")
    @ApiOperation(value = "로그인")
    public Object login(@RequestBody @ApiParam(value = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) UserDto user) {
    	// @RequestParam(required = true) final String email, @RequestParam(required = true) final String password
		final BasicResponse result = new BasicResponse();
		
    	String email = user.getEmail();
    	String password = user.getPassword();
    	Optional<UserDto> userOpt = userDao.findUserByEmailAndPassword(email, password);
        ResponseEntity response = null;
     
        
        if (userOpt.isPresent()) {
        	String token = jwtService.create("email", userOpt.get().getEmail(), "access-token");// key, data, subject

            result.status= true;
            result.message = SUCCESS;
            result.object= token;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
        	//resultMap.put("message", "존재하지 않는 사용자입니다.");
        	 result.status= false;
             result.message = FAIL;
            response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        return response;
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
	@ApiOperation(value="회원가입")
	public Object join(@RequestBody UserDto user) {
		final BasicResponse result = new BasicResponse();
		System.out.println("user: "+user);
		user.setRole("ROLE_USER");
		userDao.save(user);
		result.object = user;
		result.status=true;
		result.message =SUCCESS;
		
		return new ResponseEntity<>(result,HttpStatus.OK);
	}


	@GetMapping("/admin/findAllUser")
	@ApiOperation(value="모든 회원 반환",response= List.class)
	public Object retrieveUsers(){
		final BasicResponse result = new BasicResponse();
		List<UserDto> list = userDao.findAll();
		result.status=true;
		result.message=SUCCESS;
		result.object = list;
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/{username}")
	@ApiOperation(value ="회원이름으로 찾기")
	public Object retrieveUserByUsername(@PathVariable String username){
		final BasicResponse result = new BasicResponse();
		Optional<UserDto> findUser = userDao.findByUsername(username);
		
	
		if(!findUser.isPresent()) {
			result.status =false;
			result.message= FAIL;
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
		else {
			result.status=true;
			result.message=SUCCESS;
			result.object = findUser;
			return new ResponseEntity<>(result, HttpStatus.OK);
		}		
	}
	
	@PutMapping
	@ApiOperation(value ="회원정보 수정")
	public Object updateUser(@RequestBody UserDto userDto){
		final BasicResponse result = new BasicResponse();
		Optional<UserDto> userOpt = userDao.findById(userDto.getUid());
//		System.out.println(userOpt.toString());
		//회원번호로 검색한 것이 있다면 수정세팅
		if(!userOpt.isPresent()) {	
			result.status =false;
			result.message= FAIL;		
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
		else {	
			result.status=true;
			result.message=SUCCESS;
			result.object = userDto;
			userDao.save(userDto);
			return new ResponseEntity<>(result,HttpStatus.OK);
		}
	}
	
	@DeleteMapping("/{uid}")
	@ApiOperation(value ="회원탈퇴")
	public Object deleteUser(@PathVariable int uid){
		final BasicResponse result = new BasicResponse();
		Optional<UserDto> userOpt = userDao.findById(uid);
		
//		System.out.println(userOpt.toString());
		if(!userOpt.isPresent()) {
			result.status =false;
			result.message= FAIL;
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
		else {			
			result.status=true;
			result.message=SUCCESS;
			result.object = userOpt.get();
			userDao.delete(userOpt.get());
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		
	}

	
	
}
