package com.ssafy.mom.model;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "image")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ImageDto{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String postImage; //포스팅 사진 경로+이름
	
	@ManyToOne
	@JoinColumn(name="articleNo")
//	@JsonIgnoreProperties({"password", "images"})
	private ArticleDto articleDto;
	
	@CreationTimestamp
	private Timestamp createDate;
	@CreationTimestamp
	private Timestamp updateDate;

	
}