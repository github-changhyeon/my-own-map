package com.ssafy.mom.model;

import java.time.LocalDateTime;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class History {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int historyNo;
	
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "uidFrom") // 외래키의 주인
	private UserDto userFrom;
	
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "uidTo") // 외래키의 주인
	private UserDto userTo;
	
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "articleNo") // 외래키의 주인
	private ArticleDto articleDto;
	
	//팔로우, 찜, 댓글
	private String state;
	
	@CreationTimestamp
	private LocalDateTime regiTime;
	
	@Builder
	public History(UserDto userFrom, UserDto userTo, ArticleDto articleDto, String state) {
		super();
		this.userFrom = userFrom;
		this.userTo = userTo;
		this.articleDto = articleDto;
		this.state = state;
	}
	
	@Builder
	public History(UserDto userFrom, UserDto userTo, String state) {
		super();
		this.userFrom = userFrom;
		this.userTo = userTo;
		this.state = state;
	}
}
