package com.ssafy.mom.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.mom.model.ArticleDto;
import com.ssafy.mom.model.ImageDto;

public interface ImageDao extends JpaRepository<ImageDto, Integer>{

//	public ArrayList<ImageDto> findAllByArticleDto(ArticleDto articleDto);

	public List<ImageDto> findAllByArticleDto(ArticleDto articleDto);

	@Transactional
	public void deleteAllByArticleDto(ArticleDto articleDto);

}

