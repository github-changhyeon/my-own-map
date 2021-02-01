package com.ssafy.mom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class MyOwnMapApplication {

   public static void main(String[] args) {
      SpringApplication.run(MyOwnMapApplication.class, args);
   }

}