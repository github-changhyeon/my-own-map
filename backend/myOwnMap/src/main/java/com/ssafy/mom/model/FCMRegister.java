package com.ssafy.mom.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name ="FCM")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value ="FCMRegister : FCM로그인 정보 저장")
public class FCMRegister {

	@ApiModelProperty(value="FCM 번호")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int fcmNo;
	
	private String token;
	
	@ApiModelProperty(value="FCM 사용자")
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "uid") // 외래키의 주인
	private UserDto userDto;

	@Builder
	public FCMRegister(String token, UserDto userDto) {
		super();
		this.token = token;
		this.userDto = userDto;
	}
	
	
	
}
