package com.project.lmsystem.service;

import org.springframework.data.jpa.repository.JpaRepository;


import com.project.lmsystem.model.StudentInfo;

public interface StudentInfoRepository extends JpaRepository<StudentInfo,Long> {
	

}
