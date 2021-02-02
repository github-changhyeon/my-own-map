package com.ssafy.mom.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.mom.model.UserDto;


//CRUD 함수를 JpaRepository가 들고있음.
// @Repository라는 어노테이션이 없어도 IoC가능.
public interface UserDao extends JpaRepository<UserDto, Integer> {
	//findBy규칙 -> Username 문법
	//select * from user where username = ?
	public Optional<UserDto> findByUsername(String username);
	public Optional<UserDto> findByEmail(String email);
	public List<UserDto> findAllByEmail(String email);

	public Optional<UserDto> findUserByEmailAndPassword(String email, String password);
	public Optional<UserDto> findByUid(int uid);
	

}
