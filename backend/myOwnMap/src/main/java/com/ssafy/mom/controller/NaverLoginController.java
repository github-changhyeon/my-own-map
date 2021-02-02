package com.ssafy.mom.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ssafy.mom.config.jwt.JwtServiceImpl;
import com.ssafy.mom.dao.UserDao;
import com.ssafy.mom.model.UserDto;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@Controller
@RequestMapping(value = "/naver")
public class NaverLoginController {
	@Value("${naverClientSecret}")
	String naverClientSecret;
	@Value("&{tokenSecret}")
	String tokenSecret;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private JwtServiceImpl jwtService;
	
	//네이버에서 스프링서버로 성공code와 state를 넘겨줌
	@GetMapping(value = "/login")
    public String NaverLogin(
            @RequestParam(value = "code") String code,
            @RequestParam(value = "state") String state
    ) throws Exception {
        String clientId = "yPZ8zfbupxQS3jRvZDvP";//애플리케이션 클라이언트 아이디값";
        
        //성공code와 state clientPw로 네이버에 토큰 요청
        String apiURL;
        apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
        apiURL += "client_id=" + clientId;
        apiURL += "&client_secret=" + naverClientSecret;
        apiURL += "&code=" + code;
        apiURL += "&state=" + state;
        String access_token = "";
        String refresh_token = "";
        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            BufferedReader br;

            if(responseCode==200) { // 정상 호출
            	System.out.println(1);
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer res = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                res.append(inputLine);
            }
            br.close();
            if(responseCode==200) { // 성공적으로 토큰을 가져온다면
                //int id;            	
                String nickName, email, tmp;
                JsonParser parser = new JsonParser();               
                JsonElement accessElement = parser.parse(res.toString());                
                System.out.println(accessElement);
                access_token = accessElement.getAsJsonObject().get("access_token").getAsString();              
                tmp = getUserInfo(access_token);               
                JsonElement userInfoElement = parser.parse(tmp);
                //id = userInfoElement.getAsJsonObject().get("response").getAsJsonObject().get("id").getAsInt();             
                nickName = userInfoElement.getAsJsonObject().get("response").getAsJsonObject().get("nickname").getAsString();            
                email = userInfoElement.getAsJsonObject().get("response").getAsJsonObject().get("email").getAsString();             
                access_token = createJWTToken(nickName, email);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(access_token);
        return "redirect:http://localhost:8081/agreement?token=" + access_token;
//        return "redirect:http://192.168.0.34/";
    }
	private String getUserInfo(String access_token) {
		System.out.println(3);
        String header = "Bearer " + access_token; // Bearer 다음에 공백 추가
        try {
            String apiURL = "https://openapi.naver.com/v1/nid/me";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", header);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
            	System.out.println(4);
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer res = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                res.append(inputLine);
            }
            br.close();
            return res.toString();
        } catch (Exception e) {
            System.err.println(e);
            return "Err";
        }
    }
	private String createJWTToken(String nickname, String email) {
		System.out.println(5);
        String token = null;
        //DecodedJWT jwt = null;       
        
        try {
//            Long EXPIRATION_TIME = 1000L * 60L * 10L;
//            Date issuedAt = new Date();
//            Date notBefore = new Date(issuedAt.getTime());
//            Date expiresAt = new Date(issuedAt.getTime() + EXPIRATION_TIME);
            
            //네아로가 처음이라면 회원가입!
        	Optional<UserDto>  user = userDao.findByEmail(email);
        	UserDto userEntity = user.get();
            if(!user.isPresent()) {
            	UserDto userSave = UserDto.builder()
            			.username(nickname)
            			.email(email)
            			.role("ROLE_USER")
            			.build();
            	
            	userEntity = userDao.save(userSave);
            }
            token= jwtService.create("email", userEntity.getEmail(), "access-token");// key, data, subject
            System.out.println(6);
            System.out.println(token);
            
//            Algorithm algorithm = Algorithm.HMAC256(tokenSecret);
//            
//            token = JWT.create()
//                    .withIssuer("auth0")
//                    .withSubject(user.getUsername())
//                    .withClaim("id", user.getUid())
//                    .withClaim("email", user.getEmail())
//                    .withNotBefore(notBefore)
//                    .withExpiresAt(expiresAt)
//                    .sign(algorithm);
        } catch (Exception e) {
            System.err.println("err: " + e);
        }
        return token;
    }




}
