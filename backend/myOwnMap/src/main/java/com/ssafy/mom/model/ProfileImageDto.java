package com.ssafy.mom.model;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
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

@Entity(name = "profileImage")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileImageDto{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String profileImage; //프로필 파일 이름
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="uid")
	private UserDto userDto;
	
	@CreationTimestamp
	private Timestamp createDate;
	@CreationTimestamp
	private Timestamp updateDate;

	
}