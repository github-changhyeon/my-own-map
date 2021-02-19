package com.ssafy.mom.email;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ssafy.mom.dao.UserDao;
import com.ssafy.mom.model.UserDto;
import com.ssafy.mom.util.RedisUtil;

import javassist.NotFoundException;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private MailSender emailSender;
    
    @Autowired
    private RedisUtil redisUtil;
    
    @Autowired
    private UserDao userDao;
   
    @Override
    public void sendMail(String to,String sub, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(sub);
        message.setText(text);
        emailSender.send(message);
    }
    
    @Override
    public void sendVerificationMail(UserDto user) throws NotFoundException {
        String VERIFICATION_LINK = "http://localhost:8080/account/verify/";
        if(user==null) { 
        	
        	throw new NotFoundException("멤버가 조회되지 않음");
        }
        UUID uuid = UUID.randomUUID();
        redisUtil.setDataExpire(uuid.toString(),user.getUsername(), 60 * 30L);
        sendMail(user.getEmail(),"[My Own Map] 회원가입 인증메일입니다.",VERIFICATION_LINK+uuid.toString());
    }

    @Override
    public void verifyEmail(String key) throws NotFoundException {
        String username = redisUtil.getData(key);
        Optional<UserDto> userOpt = userDao.findByUsername(username);
        if(!userOpt.isPresent()) throw new NotFoundException("멤버가 조회되지않음");
        userOpt.get().setRole("ROLE_USER"); //정회원        
        userDao.save(userOpt.get());
        redisUtil.deleteData(key);
    }
    
}
