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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name ="comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value ="CommentDto : 댓글 정보", description="게시글내 댓글정보를 나타낸다.")
public class CommentDto {
	
	@ApiModelProperty(value="댓글 번호")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int commentNo;
	
	@ApiModelProperty(value="댓글 내용")
	private String context;
	
	@ApiModelProperty(value="댓글 작성자")
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "uid") // 외래키의 주인
	private UserDto userDto;
	
	@ApiModelProperty(value="댓글을 포함하는 게시글 번호")
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "articleNo") // 외래키의 주인
	private ArticleDto articleDto;
	
	@ApiModelProperty(value="댓글 작성일")
	@CreationTimestamp
	private LocalDateTime regiTime;
	
}
