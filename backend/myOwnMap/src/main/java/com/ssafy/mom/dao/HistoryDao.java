package com.ssafy.mom.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ssafy.mom.model.History;
import com.ssafy.mom.model.UserDto;

import io.lettuce.core.dynamic.annotation.Param;

public interface HistoryDao extends JpaRepository<History, Integer> {
	
	
	@Query(value = "SELECT * FROM History where userTo = (:userDto) ORDER BY regiTime DESC" )
	List<History> findAllByUserTo(@Param("userDto") UserDto userDto);

}
