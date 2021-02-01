package com.ssafy.mom.dao;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.mom.model.HashtagDto;
import com.ssafy.mom.model.UserDto;
import com.ssafy.mom.model.UserHashtag;

public interface UserHashtagDao extends JpaRepository<UserHashtag, String> {

	List<UserHashtag> findAllByUserDto(UserDto userDto);

	Optional<UserHashtag> findByUserDtoAndHashtagDto(UserDto userDto, HashtagDto alreadyExist);

	List<HashtagDto> findHashtagDtoByUserDto(UserDto userDto);

	@Transactional
	void deleteByHashtagDto(HashtagDto hashtagDto);

	int countByHashtagDto(HashtagDto hashtagDto);
	
//	@Transactional
//	void deleteByUserDto(UserDto userDto);
	
	 
}
