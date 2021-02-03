package com.ssafy.mom.model;


import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "article")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int articleNo;
	private String title;
	private String contents;
	private String positionLat;
	private String positionLng;
	private String address;
//	@CreationTimestamp
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	private Timestamp regiTime;
//	private String regitime;

//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@CreationTimestamp
	private LocalDateTime regiTime;
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@UpdateTimestamp
	private LocalDateTime updateTime;
	
	private String visitDate;
	
	private double evaluation;
	@Transient
	private ArrayList<HashtagDto> hashtags;
	@Transient
	private ArrayList<String> imagePaths;
	@Transient
	private int uid;
	
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "uid") // 외래키의 주인
	private UserDto userDto;
	
	
}