package com.ssafy.mom.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

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

	@Builder
	public UserDto(String username, String role, String email) {
		super();
		this.username = username;
		this.role = role;
		this.email = email;
	}

}
