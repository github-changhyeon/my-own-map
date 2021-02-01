package com.ssafy.mom.dao;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.mom.model.HashtagDto;
import com.ssafy.mom.model.UserDto;

public interface HashtagDao extends JpaRepository<HashtagDto, String> {

	HashtagDto findByHashtagNo(Integer integer);

	HashtagDto findByHashtagName(String hashtagName);

}

