package com.ssafy.mom.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.mom.model.ProfileImageDto;

public interface ProfileImageDao extends JpaRepository<ProfileImageDto, Integer>{

//	public ArrayList<ImageDto> findAllByArticleDto(ArticlesDto articleDto);

//	public List<ImageDto> findAllByArticleDto(ArticleDto articleDto);

//	@Transactional
//	public void deleteAllByArticleDto(ArticleDto articleDto);

}

