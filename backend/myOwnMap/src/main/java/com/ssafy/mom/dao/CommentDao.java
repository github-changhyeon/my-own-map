package com.ssafy.mom.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.mom.model.ArticleDto;
import com.ssafy.mom.model.CommentDto;

public interface CommentDao extends JpaRepository<CommentDto, Integer>{

	List<CommentDto> findAllByArticleDto(ArticleDto articleDto);
	@Transactional
	void deleteByArticleDto(ArticleDto articleDto);

}
