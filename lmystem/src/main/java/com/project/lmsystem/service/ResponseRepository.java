package com.project.lmsystem.service;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.lmsystem.model.Response;

public interface ResponseRepository extends JpaRepository<Response, Long>{
	
	@Query(value="SELECT r from Response r where r.responsetype=:responsetype")
	List<Response> findResponsebyResponseType(@Param("responsetype") String responsetype);
	}



