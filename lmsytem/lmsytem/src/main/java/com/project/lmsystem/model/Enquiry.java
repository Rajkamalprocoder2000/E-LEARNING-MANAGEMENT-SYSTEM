package com.project.lmsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 

@Table(name="enquiries")

public class Enquiry {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(length = 50 , nullable = false)
	private String name;
	
	@Column(length = 15 , nullable = false)
	private String contactno;
	
	@Column(length = 50,nullable = false)
	private String email;
	
	@Column(length  = 50 , nullable = false)
	private String enquirytype;
	
	@Column(length=500 , nullable = false)
	private String enquirytext;
	
	@Column(length = 30 , nullable = false)
	private String enquirydate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactno() {
		return contactno;
	}

	public void setContactno(String contactno) {
		this.contactno = contactno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEnquirytype() {
		return enquirytype;
	}

	public void setEnquirytype(String enquirytype) {
		this.enquirytype = enquirytype;
	}

	public String getEnquirytext() {
		return enquirytext;
	}

	public void setEnquirytext(String enquirytext) {
		this.enquirytext = enquirytext;
	}

	public String getEnquirydate() {
		return enquirydate;
	}

	public void setEnquirydate(String enquirydate) {
		this.enquirydate = enquirydate;
	}
	
	
}
