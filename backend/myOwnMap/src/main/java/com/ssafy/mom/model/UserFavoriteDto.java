package com.ssafy.mom.model;

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
import lombok.ToString;

@Entity(name="userFavorite")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value ="UserFavoriteDto : 찜하기", description="게시글내 댓글정보를 나타낸다.")
public class UserFavoriteDto {
	
	@ApiModelProperty(value="찜 번호")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int favoriteNo;
	
	@ApiModelProperty(value="찜한 사람")
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "uid") // 외래키의 주인
	private UserDto userDto;
	
	@ApiModelProperty(value="찜한 게시글")
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "articleNo") // 외래키의 주인
	private ArticleDto articleDto;
	
	
	
}
