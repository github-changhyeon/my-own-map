package com.ssafy.mom.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.mom.model.ArticleDto;
import com.ssafy.mom.model.UserDto;
import com.ssafy.mom.model.UserFavoriteDto;



public interface FavoriteDao extends JpaRepository<UserFavoriteDto, Integer>{

	Optional<UserFavoriteDto> findByUserDtoAndArticleDto(UserDto userDto, ArticleDto articleDto);

	List<UserFavoriteDto> findAllByUserDto(UserDto userDto);

}
