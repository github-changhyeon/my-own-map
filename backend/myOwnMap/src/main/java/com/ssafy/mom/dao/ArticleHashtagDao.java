package com.ssafy.mom.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.mom.model.ArticleDto;
import com.ssafy.mom.model.ArticleHashtag;
import com.ssafy.mom.model.HashtagDto;

import io.lettuce.core.dynamic.annotation.Param;

public interface ArticleHashtagDao extends JpaRepository<ArticleHashtag, String> {

	List<ArticleHashtag> findAllByArticleDto(ArticleDto nowArticle);

	List<HashtagDto> findHashtagDtoByArticleDto(ArticleDto nowArticle);

	@Transactional
	void deleteByArticleDto(ArticleDto articleDto);

	
	List<ArticleHashtag> findAllByHashtagDto(HashtagDto hashtagDto);
//	List<Integer> findHashtagNoByUserDto(UserDto userDto);

	ArticleHashtag findByArticleDtoAndHashtagDto(ArticleDto articleDto, HashtagDto alreadyExist);

	 
}
