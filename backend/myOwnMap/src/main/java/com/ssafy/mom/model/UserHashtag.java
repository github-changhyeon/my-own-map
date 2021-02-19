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
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "uid") // 외래키의 주인
	private UserDto userDto;
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "hashtagNo")
	private HashtagDto hashtagDto;
	
	private int publicCnt;
	// 남의 페이지에서만!
	// 게시물1 private 산책    : publicCnt 0    ->  가리구나
	// 게시물2 public 산책    : publicCnt 1 ----->  보이구나
	// 게시물3 private 산책 : publicCnt 1 ------>  보임
	
	// 게시물1 private 산책  삭제   : publicCnt 1 ->  보이구나
	// 게시물2 public 산책    삭제 : publicCnt 0 ----->  가리구나

	// 내 지도에서는
	// publicCnt 신경 안씀
	
	// userHashtag 중복확인 -> 존재 안한다면 userhashtag에 넣어줌
	// userhashtag에 존재한다면 public->public+1 / private-> -
	// userhashtag에 존재하지않는다 public -> public = 1  /  private -> public = 0
	
	
	
	
//	private int privateCnt;
	
	
	public UserHashtag(UserDto userDto, HashtagDto hashtagDto, int publicCnt) {
		super();
		this.userDto = userDto;
		this.hashtagDto = hashtagDto;
		this.publicCnt = publicCnt;
	}
	
}