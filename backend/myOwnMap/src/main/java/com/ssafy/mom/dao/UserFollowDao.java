package com.ssafy.mom.dao;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.mom.model.UserDto;
import com.ssafy.mom.model.UserFollow;


public interface UserFollowDao extends JpaRepository<UserFollow, Integer> {
	//내가 팔로우한 사람들 찾기(팔로잉)
	public List<UserFollow> findAllByUserFrom(UserDto userFrom);
	//나를 팔로우한 사람들 찾기(팔로워)
	public List<UserFollow> findAllByUserTo(UserDto userTo);
	
	
	@Transactional
	public void deleteByUserFromAndUserTo(UserDto userFrom, UserDto userTo);
}
