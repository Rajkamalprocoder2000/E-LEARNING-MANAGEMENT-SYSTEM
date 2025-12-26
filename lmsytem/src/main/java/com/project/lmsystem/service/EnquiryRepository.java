package com.project.lmsystem.service;

import org.springframework.data.jpa.repository.JpaRepository;



import com.project.lmsystem.model.Enquiry;

public interface EnquiryRepository extends JpaRepository<Enquiry, Integer> {

}
