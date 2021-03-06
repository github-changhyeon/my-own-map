package com.ssafy.mom.model;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "user")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int uid;
	private String username;
	private String password;
	private String role;
	@Column(name = "email", unique = true, nullable = false)
	private String email;
	@CreationTimestamp
	private LocalDateTime joinDate;
	private String stateMsg;
	private boolean isLogin;
	private int notificationCheck;

	@Transient
	private String profileImagePath;


	
	@Builder
	public UserDto(String username, String role, String email) {
		super();
		this.username = username;
		this.role = role;
		this.email = email;
	}

}
