package com.ssafy.mom.model;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name="userFollow")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFollow {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name = "uidFrom") // 외래키의 주인
	private UserDto userFrom;
	@ManyToOne
	@JoinColumn(name = "uidTo")
	private UserDto userTo;
	
	@Builder
	public UserFollow(UserDto userFrom, UserDto userTo) {
		super();
		this.userFrom = userFrom;
		this.userTo = userTo;
	}
	
	
}