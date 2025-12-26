package com.project.lmsystem.controller;

import java.io.InputStream;



import java.net.URI;
import java.nio.file.CopyOption;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.lmsystem.api.SmsSender;

import com.project.lmsystem.dto.EnquiryDto;
import com.project.lmsystem.dto.MaterialDto;

import com.project.lmsystem.model.Enquiry;
import com.project.lmsystem.model.Material;
import com.project.lmsystem.model.Response;
import com.project.lmsystem.model.StudentInfo;
import com.project.lmsystem.service.AdminLoginRepository;

import com.project.lmsystem.service.EnquiryRepository;
import com.project.lmsystem.service.MaterialRepository;
import com.project.lmsystem.service.ResponseRepository;
import com.project.lmsystem.service.StudentInfoRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private static final CopyOption StadardCopyOption = null;
	@Autowired
	AdminLoginRepository alrepo;
	@Autowired
	EnquiryRepository erepo;
	@Autowired
	ResponseRepository rrepo;
	@Autowired
	StudentInfoRepository sirepo;
	@Autowired
	MaterialRepository mrepo;
	

	private URI uploadDir;
	private Paths path;
	private Object rlist;
	@GetMapping("/")
	public String showAdminHome(HttpSession session, Model model, HttpServletResponse response) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("adminid") != null) {
				String scount=sirepo.count()+"";
				String ecount=erepo.count()+"";
				String rcount=rrepo.count()+"";
				String mcount=mrepo.count()+"";
				model.addAttribute("scount",scount);
				model.addAttribute("ecount",ecount);
				model.addAttribute("rcount",rcount);
				model.addAttribute("mcount",mcount);
				return "/admin/adminhome";
			} else {
				return "redirect:/adminlogin";
			}
		} catch (Exception ex) {
			return "redirect:/adminlogin";
		}
	}
	@GetMapping("/adminlogout")
	public String adminlogout(HttpSession session, Model model, HttpServletResponse response) {
		try {
					session.invalidate();
					return "redirect:/adminlogin";
				} catch (Exception ex) {
					return "redirect:/adminlogin";
				}
			}
	@GetMapping("/managestudents")
	public String showManageStudents(HttpSession session, Model model, HttpServletResponse response) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("adminid") != null) {
				
				List<StudentInfo> siList=sirepo.findAll();
				model.addAttribute("siList",siList);
				return "/admin/managestudents";
			} else {
				return "redirect:/adminlogin";
			}
		} catch (Exception ex) {
			return "redirect:/adminlogin";
		}
	}
	@GetMapping("/viewenquiries")
	public String showEnquiries(HttpSession session, Model model, HttpServletResponse response) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("adminid") != null) {
				
				List<Enquiry> elist=erepo.findAll();
				model.addAttribute("elist",elist);
				return "/admin/viewenquiries";
			} else {
				return "redirect:/adminlogin";
			}
		} catch (Exception ex) {
			return "redirect:/adminlogin";
		}
	}
	@GetMapping("/deleteenq")
	public String deleteEnquiry(HttpSession session, Model model, HttpServletResponse response,@RequestParam int id) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("adminid") != null) {
				
				Enquiry e=erepo.getById(id);
				erepo.delete(e);
				return "redirect:/admin/viewenquiries";
			} else {
				return "redirect:/adminlogin";
			}
		} catch (Exception ex) {
			return "redirect:/adminlogin";
		}
	}

	@GetMapping("/viewfeedback")
	public String showFeedback(HttpSession session, Model model, HttpServletResponse response) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("adminid") != null) {
				
				List<Response>rlist=rrepo.findAll();
				model.addAttribute("rlist",rlist);
				
				return "/admin/viewfeedback";
			} else {
				return "redirect:/adminlogin";
			}
		} catch (Exception ex) {
			return "redirect:/adminlogin";
		}
	
	}
	@GetMapping("/deletefeedback")
	public String deleteFeedback1(HttpSession session, Model model, HttpServletResponse response,@RequestParam long id) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("adminid") != null) {
				
			Response r=rrepo.getById(id);
			rrepo.delete(r);
			
				return "redirect:/admin/viewfeedback";
			} else {
				return "redirect:/adminlogin";
			}
		} catch (Exception ex) {
			return "redirect:/adminlogin";
		}
	}
	
	
	
	
	
	@GetMapping("/addmaterials")
	public String showAddMaterials(HttpSession session, Model model, HttpServletResponse response) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("adminid") != null) {
				MaterialDto dto=new MaterialDto();
				model.addAttribute("dto",dto);
				return "/admin/addmaterials";
			} else {
				return "redirect:/adminlogin";
			}
		} catch (Exception ex) {
			return "redirect:/adminlogin";
		}
	}
	@PostMapping("/addmaterials")
	public String createAddMaterials(HttpSession session, Model model, HttpServletResponse response,@ModelAttribute MaterialDto dto,RedirectAttributes redirectAttributes) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("adminid") != null) {
				MultipartFile matfile=dto.getMatfile();
				String storageFileName=new Date().getTime()+" "+matfile.getOriginalFilename();
				String uploadDir="public/mat/";
				Path uploadPath=Paths.get(uploadDir);
				if(!Files.exists(uploadPath))
				{
					Files.createDirectories(uploadPath);
				}
				try(InputStream inputStream=matfile.getInputStream()){
					Files.copy(inputStream, Paths.get(uploadDir+storageFileName),StandardCopyOption.REPLACE_EXISTING);
				}
				
				Material material=new Material();
				material.setProgram(dto.getProgram());
				material.setBranch(dto.getBranch());
				material.setYear(dto.getYear());
				material.setSubject(dto.getSubject());
				material.setTopic(dto.getTopic());
				material.setMaterialtype(dto.getMaterialtype());
				material.setFilename(storageFileName);
				material.setPosteddate(new Date()+"");
				mrepo.save(material);
				redirectAttributes.addFlashAttribute("msg","Material is Uploaded");
				return "redirect:/admin/addmaterials";
			} else {
				return "redirect:/adminlogin";
			}
		} catch (Exception ex) {
			return "redirect:/adminlogin";
		}
	}
	@GetMapping("/managematerials")
	public String showMaterials(HttpSession session, Model model, HttpServletResponse response) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("adminid") != null) {
				List<Material> mlist=mrepo.findAll();
				model.addAttribute("mlist", mlist);
				return "/admin/managematerials";
			} else {
				return "redirect:/adminlogin";
			}
		} catch (Exception ex) {
			return "redirect:/adminlogin";
		}
	}
	@GetMapping("/deletemat")
	public String deleteMaterial(HttpSession session, Model model, HttpServletResponse response,@RequestParam int id) {
		try {
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("adminid") != null) {
				Material m=mrepo.getById(id);
			    Path imagePath=Paths.get("public/mat/"+m.getFilename());
			    try {
			    	Files.delete(imagePath);
			    }
			    catch(Exception ex){
			    	ex.printStackTrace();
			    }
			    mrepo.delete(m);
				return "redirect:/admin/managematerials";
			} else {
				return "redirect:/adminlogin";
			}
		} catch (Exception ex) {
			return "redirect:/adminlogin";
		}
	}
	
	
	
}


