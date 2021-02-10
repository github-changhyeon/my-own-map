package com.ssafy.mom.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.mom.model.ProfileImageDto;
import com.ssafy.mom.model.UserDto;

public interface ProfileImageDao extends JpaRepository<ProfileImageDto, Integer>{

	ProfileImageDto findByUserDto(UserDto userDto);

	int findIdByUserDto(UserDto userDto);

//	public ArrayList<ImageDto> findAllByArticleDto(ArticlesDto articleDto);

//	public List<ImageDto> findAllByArticleDto(ArticleDto articleDto);

//	@Transactional
//	public void deleteAllByArticleDto(ArticleDto articleDto);

}

