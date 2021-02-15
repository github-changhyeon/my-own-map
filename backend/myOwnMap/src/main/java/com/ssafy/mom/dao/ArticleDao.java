
package com.ssafy.mom.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.mom.model.ArticleDto;
import com.ssafy.mom.model.UserDto;

public interface ArticleDao extends JpaRepository<ArticleDto, String> {
    
	Optional<ArticleDto> findByArticleNo(int articleNo);
	List<ArticleDto> findTop10ByUserDtoOrderByUpdateTimeDesc(UserDto user);

	List<ArticleDto> findAllByUserDto(UserDto userDto);
//	int update(int articleNo, ArticleDto articleDto);
	List<ArticleDto> findTop10ByUserDtoAndIsPrivateOrderByUpdateTimeDesc(UserDto userDto, boolean b);
	List<ArticleDto> findAllByUserDtoAndIsPrivate(UserDto userDto, boolean b);
	List<ArticleDto> findAllByIsPrivate(boolean b);
}
