package com.project.lmsystem.controller;

import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.lmsystem.dto.ResponseDto;
import com.project.lmsystem.dto.StudentInfoDto;
import com.project.lmsystem.model.Material;
import com.project.lmsystem.model.Response;
import com.project.lmsystem.model.StudentInfo;
import com.project.lmsystem.service.MaterialRepository;
import com.project.lmsystem.service.ResponseRepository;
import com.project.lmsystem.service.StudentInfoRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller

@RequestMapping("/student/")

public class StudentController {

	@Autowired
	StudentInfoRepository sirepo;
	@Autowired
	ResponseRepository rrepo;
	@Autowired
    MaterialRepository mrepo;
	@GetMapping("/")
	public String showStudentHome(HttpSession session, Model model, HttpServletResponse response) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("rollno") != null) {
				StudentInfo s = sirepo.getById(Long.parseLong(session.getAttribute("rollno").toString()));
				model.addAttribute("s", s);
				StudentInfoDto dto=new StudentInfoDto();
				model.addAttribute("dto", dto);
				return "/student/studenthome";
			} else {
				return "redirect:/login";
			}
		} catch (Exception ex) {
			return "redirect:/login";
		}
	}

	@GetMapping("/logout")
	public String Logout(HttpSession session) {
		try {
			session.invalidate();
			return "redirect:/login";
		} catch (Exception ex) {
			return "redirect:/login";
		}
	}

	@GetMapping("/giveresponse")
	public String showGiveResponse(HttpSession session, Model model, HttpServletResponse response) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("rollno") != null) {
				StudentInfo s = sirepo.getById(Long.parseLong(session.getAttribute("rollno").toString()));
				ResponseDto responseDto = new ResponseDto();
				model.addAttribute("s", s);
				model.addAttribute("responseDto", responseDto);
				return "/student/giveresponse";
			} else {
				return "redirect:/login";
			}
		} catch (Exception ex) {
			return "redirect:/login";
		}
	}

	@PostMapping("/giveresponse")
	public String createGiveResponse(HttpSession session, Model model, HttpServletResponse response,
			@ModelAttribute ResponseDto responseDto, RedirectAttributes redirectAttributes) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("rollno") != null) {
				StudentInfo s = sirepo.getById(Long.parseLong(session.getAttribute("rollno").toString()));
				model.addAttribute("s", s);
				Response rs = new Response();
				rs.setName(s.getName());
				rs.setContactno(s.getContactno());
				rs.setEmailaddress(s.getEmailaddress());
				rs.setProgram(s.getProgramme());
				rs.setBranch(s.getBranch());
				rs.setYear(s.getYear());
				rs.setResponsetype(responseDto.getResponsetype());
				rs.setResponsetext(responseDto.getResponsetext());
				rs.setResponsedate(new Date()+"");
				rrepo.save(rs);
				redirectAttributes.addFlashAttribute("msg","Your response is submitted");
				return "redirect:/student/giveresponse";
			} else {
				return "redirect:/login";
			}
		} catch (Exception ex) {
			return "redirect:/login";
		}
	}
	@GetMapping("/changepassword")
	public String showChangePassword(HttpSession session, Model model, HttpServletResponse response) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("rollno") != null) {
				StudentInfo s = sirepo.getById(Long.parseLong(session.getAttribute("rollno").toString()));
				model.addAttribute("s", s);
				return "/student/changepassword";
			} else {
				return "redirect:/login";
			}
		} catch (Exception ex) {
			return "redirect:/login";
		}
	}
	@PostMapping("/changepassword")
	public String changePassword(HttpSession session, Model model, HttpServletResponse response,HttpServletRequest request,RedirectAttributes redirectAttributes) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("rollno") != null) {
				StudentInfo s = sirepo.getById(Long.parseLong(session.getAttribute("rollno").toString()));
				model.addAttribute("s", s);
				String oldpassword=request.getParameter("oldpassword");
				String newpassword=request.getParameter("newpassword");
				String confirpassword=request.getParameter("confirmpassword");
				if(!newpassword.equals(confirpassword)) {
					redirectAttributes.addFlashAttribute("msg","NewPassword and confirmpassword are not same");
					return"redirect:/student/changepassword";
				}
				if(!oldpassword.equals(s.getPassword())) {
					redirectAttributes.addFlashAttribute("msg","Oldpassword is not matched");
					return"redirect:/student/changepassword";
				}
				s.setPassword(newpassword);
				sirepo.save(s);
				return "redirect:/student/logout";
			} else {
				return "redirect:/login";
			}
		} catch (Exception ex) {
			return "redirect:/login";
		}
	}
	@GetMapping("/myprofile")
	public String showProfile(HttpSession session, Model model, HttpServletResponse response) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("rollno") != null) {
				StudentInfo s = sirepo.getById(Long.parseLong(session.getAttribute("rollno").toString()));
				model.addAttribute("name", s.getName());
				model.addAttribute("s",s);
				return "/student/myprofile";
			} else {
				return "redirect:/login";
			}
		} catch (Exception ex) {
			return "redirect:/login";
		}
	}
	@GetMapping("/viewassignments")
	public String showAssignments(HttpSession session, Model model, HttpServletResponse response) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("rollno") != null) {
				StudentInfo s = sirepo.getById(Long.parseLong(session.getAttribute("rollno").toString()));
				List<Material>mlist=mrepo.findMaterial(s.getProgramme(), s.getBranch(), s.getYear(), "assign");
				model.addAttribute("mlist",mlist);
				model.addAttribute("s", s);
				return "/student/viewassignments";
			} else {
				return "redirect:/viewassignments";
			}
		} catch (Exception ex) {
			return "redirect:/login";
		}
	}
	
	
	@GetMapping("/viewstudymaterial")
	public String showMaterial(HttpSession session,Model model,HttpServletResponse response) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("rollno") != null) {
				StudentInfo s = sirepo.getById(Long.parseLong(session.getAttribute("rollno").toString()));
				List<Material>mlist=mrepo.findMaterial(s.getProgramme(), s.getBranch(), s.getYear(), "smat");
				model.addAttribute("mlist",mlist);
				model.addAttribute("s", s);
				return "/student/viewstudymaterial";
			} else {
				return "redirect:/viewassignments";
			}
		} catch (Exception ex) {
			return "redirect:/login";
		}
	}
	@PostMapping("/")
	public String uploadProfilePic(HttpSession session, Model model, HttpServletResponse response,@ModelAttribute StudentInfoDto dto) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("rollno") != null) {
				StudentInfo s = sirepo.getById(Long.parseLong(session.getAttribute("rollno").toString()));
				MultipartFile image=dto.getProfilepic();
				String storageFileName=new Date().getTime()+"_"+image.getOriginalFilename();
				String uploadDir="public/images/";
				Path uploadPath=Paths.get(uploadDir);
				if(!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				try(InputStream inputStream=image.getInputStream()){
					Files.copy(inputStream, Paths.get(uploadDir+storageFileName),StandardCopyOption.REPLACE_EXISTING);
				}
				s.setProfilepic(storageFileName);
				sirepo.save(s);
				return "redirect:/student/";
			} else {
				return "redirect:/login";
			}
		} catch (Exception ex) {
			return "redirect:/login";
		}
	}

}
