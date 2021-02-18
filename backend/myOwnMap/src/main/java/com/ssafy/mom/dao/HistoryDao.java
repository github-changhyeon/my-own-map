package com.ssafy.mom.dao;

import java.beans.Transient;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ssafy.mom.model.ArticleDto;
import com.ssafy.mom.model.History;
import com.ssafy.mom.model.UserDto;

import io.lettuce.core.dynamic.annotation.Param;

public interface HistoryDao extends JpaRepository<History, Integer> {
	
	
	List<History> findAllByUserToOrderByRegiTimeDesc( UserDto userDto);
	@Transactional
	void deleteByArticleDto(ArticleDto articleDto);

}
