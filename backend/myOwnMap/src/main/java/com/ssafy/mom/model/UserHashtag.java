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

@Entity(name="userHashtag")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserHashtag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name = "uid") // 외래키의 주인
	private UserDto userDto;
	@ManyToOne
	@JoinColumn(name = "hashtagNo")
	private HashtagDto hashtagDto;
	public UserHashtag(UserDto userDto, HashtagDto hashtagDto) {
		super();
		this.userDto = userDto;
		this.hashtagDto = hashtagDto;
	}
	
}