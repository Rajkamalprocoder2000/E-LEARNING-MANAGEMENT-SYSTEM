package com.project.lmsystem.service;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.lmsystem.model.Material;

public interface MaterialRepository extends JpaRepository<Material, Integer>{
	@Query("Select m from Material m where m.program= :program and m.branch= :branch and m.year=:year and m.materialtype=:materialtype ")
    List<Material> findMaterial(@Param("program")String program, @Param("branch")String branch, @Param("year")String year, @Param("materialtype")String materialtype);
}
