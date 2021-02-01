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

@Entity(name="articleHashtag")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleHashtag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "articleNo") // 외래키의 주인
	private ArticleDto articleDto;
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "hashtagNo")
	private HashtagDto hashtagDto;
	public ArticleHashtag(ArticleDto articleDto, HashtagDto hashtagDto) {
		super();
		this.articleDto = articleDto;
		this.hashtagDto = hashtagDto;
	}
}