package com.ssafy.mom.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.mom.model.ImageDto;

public interface ImageDao extends JpaRepository<ImageDto, Integer>{

}

