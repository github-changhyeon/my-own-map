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

	
	@Query(value = "SELECT count(*) FROM articleHashtag where hashtagNo = (:hashtagNo)")
	long findAllByHashtagNo(@Param("hashtagNo") int hashtagNo);
//	List<Integer> findHashtagNoByUserDto(UserDto userDto);

	 
}
