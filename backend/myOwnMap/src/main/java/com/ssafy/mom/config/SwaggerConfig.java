package com.ssafy.mom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicates;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *  @Bean을 사용하는 클래스에는
 *  반드시 @Configuration 어노테이션을 활용하여 해당 클래스에서 Bean을 등록하고자 함을 명시해주어야 한다.
 *  
 *  Bean
 *  - spring 애플리케이션을 구성하는 핵심 객체
 *  - spring IoC 컨테이너에 의해 생성 및 관리
 *  - class id scope(Beam을 생성하기 위한 방법) constructor-arg(생성시 생성자에 전달할 파라미터) property(Bean생성시 setter에 전달할 인수)
 *  
 *  @Bean
 *  - 개발자가 직접 제어가 불가능한 라이브러리를 활용할때
 *  - 초기에 설정을 하기 위해 활용할 때
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
				.paths(PathSelectors.any()).build();
	}

}
