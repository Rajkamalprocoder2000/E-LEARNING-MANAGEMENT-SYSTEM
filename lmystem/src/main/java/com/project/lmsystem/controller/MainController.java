package com.project.lmsystem.controller;

import java.util.Date;


import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.lmsystem.api.SmsSender;
import com.project.lmsystem.dto.AdminLoginDto;
import com.project.lmsystem.dto.EnquiryDto;
import com.project.lmsystem.dto.StudentInfoDto;
import com.project.lmsystem.model.AdminLogin;
import com.project.lmsystem.model.Enquiry;
import com.project.lmsystem.model.StudentInfo;
import com.project.lmsystem.service.AdminLoginRepository;
import com.project.lmsystem.service.EnquiryRepository;
import com.project.lmsystem.service.StudentInfoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;

@Controller

public class MainController {

	@Autowired
	EnquiryRepository erepo;
	@Autowired
	StudentInfoRepository sirepo;
	@Autowired
	AdminLoginRepository alrepo;
	private HttpSession session;

	@GetMapping("/")

	public String showIndex() {
		return "index";
	}
	
	@GetMapping("/about")
	public String showabout() {
		return "about";
	}

	@GetMapping("/courses")
	public String showcourses() {
		return "courses";
	}
	@GetMapping("/contact")

	public String showcontact(Model model) {
		EnquiryDto enquiryDto=new EnquiryDto();
		model.addAttribute("enquiryDto", enquiryDto);
		return "contact";
	}

	@PostMapping("/contact")
	public String createContact(@ModelAttribute EnquiryDto enquiryDto, RedirectAttributes redirectAttributes ) {
		Enquiry e=new Enquiry();
		e.setName(enquiryDto.getName());
		e.setContactno(enquiryDto.getContactno());
		e.setEmail(enquiryDto.getEmail());
		e.setEnquirytype(enquiryDto.getEnquirytype());
		e.setEnquirytext(enquiryDto.getEnquirytext());
		e.setEnquirydate(new Date()+"");
		erepo.save(e);
		SmsSender ss=new SmsSender();
		ss.sendSms(enquiryDto.getContactno());
		redirectAttributes.addFlashAttribute("msg", "Enquiry is saved");
		return "redirect:/contact";
	}
	
	@GetMapping("/registration")
	public String showRegistration(Model model)
	{
		StudentInfoDto infoDto=new StudentInfoDto();
		model.addAttribute("infoDto", infoDto);
		return "registration";
	}
	
	@PostMapping("/registration")
	public String createRegistration(@ModelAttribute StudentInfoDto infoDto, RedirectAttributes redirectAttributes) {
		StudentInfo s=new StudentInfo();
		s.setRollno(infoDto.getRollno());
		s.setName(infoDto.getName());
		s.setFname(infoDto.getFname());
		s.setMname(infoDto.getMname());
		s.setGender(infoDto.getGender());
		s.setAddress(infoDto.getAddress());
		s.setProgramme(infoDto.getProgramme());
		s.setBranch(infoDto.getBranch());
		s.setYear(infoDto.getYear());
		s.setContactno(infoDto.getContactno());
		s.setEmailaddress(infoDto.getEmailaddress());
		s.setPassword(infoDto.getPassword());
		s.setRegdate(new Date()+"");
		s.setStatus("false");
		sirepo.save(s);
		redirectAttributes.addFlashAttribute("msg", "Registration is Done");
		return "redirect:/registration";
	}
	@GetMapping("/login")
	public String showLogin() {
		return "login";
	}
	@PostMapping("/login")
	public String validateUser(@ModelAttribute StudentInfoDto infoDto, RedirectAttributes redirectAttributes,HttpSession session)
	{
		try {
			StudentInfo s= sirepo.getById(infoDto.getRollno());
			if(s.getPassword().equals(infoDto.getPassword()))
			{
				session.setAttribute("rollno", infoDto.getRollno()+"");
				return "redirect:/student/";
			}
			else
			{
				redirectAttributes.addFlashAttribute("msg", "Invalid User");
			}
		}
		catch(EntityNotFoundException ex)
		{
			redirectAttributes.addFlashAttribute("msg", "Student does not exist");
		}
		return "redirect:/login";
	}
	@GetMapping("/adminlogin")

	public String showAdminLogin(Model model) {
		
		AdminLoginDto dto=new AdminLoginDto();
		model.addAttribute("dto",dto);
		return "adminlogin";
	}
	@PostMapping("/adminlogin")
	public String validateAdmin(@ModelAttribute AdminLoginDto dto , RedirectAttributes redirectAttributes ,HttpSession session)
	{
		try
		{
		AdminLogin al=alrepo.getById(dto.getAdminid());
		if(al.getAdminpassword().equals(dto.getAdminpassword()))
		{
			//redirectAttributes.addFlashAttribute("msg" , "Valid User");
			session.setAttribute("adminid", al.getAdminid());
			return "redirect:admin/";
		}
		else
		{
			redirectAttributes.addFlashAttribute("msg" , "Invalid User");
			return "redirect:adminlogin";
		}
		}
		catch(EntityNotFoundException ex)
		{
			redirectAttributes.addFlashAttribute("msg" ,"Admin not found");
			return "redirect:/adminlogin";
		}
	}
}
