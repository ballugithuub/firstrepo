package in.sp.main.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.sp.main.entity.CustEnquiry;
import in.sp.main.entity.Employee;
import in.sp.main.service.CustEnquiryService;
import in.sp.main.service.ProductService;
import jakarta.servlet.http.HttpSession;

@Controller
public class CustEnquiryController
{
	@Autowired
	ProductService productService;
	
	@Autowired
	CustEnquiryService custEnquiryService;
	
	@GetMapping("/customerEnquiryPage")
	public String customerEnquiryPage(
			Model model,
			@RequestParam(name = "redirect_success", required = false) String success,
			@RequestParam(name = "redirect_error", required = false) String error)
	{
		List<String> list_coursenames = productService.getAllCourseNameService();
		model.addAttribute("model_coursename_list", list_coursenames);
		
		model.addAttribute("modelCustEnquiryAttr", new CustEnquiry());
		
		model.addAttribute("model_success", success);
		model.addAttribute("model_error", error);
		
		return "customer-enquiry";
	}
	
	@PostMapping("/custEnquiryForm")
	public String custEnquiryForm(
			HttpSession session,
			@ModelAttribute("modelCustEnquiryAttr") CustEnquiry custEnquiry,
			RedirectAttributes redirectAttributes
			)
	{
		LocalDate date = LocalDate.now();
		String date1 = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		LocalTime time = LocalTime.now();
		String time1 = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		
		Employee employee = (Employee) session.getAttribute("session_employee");
		String empemail = "";
		if(employee != null)
		{
			empemail = employee.getEmail();
		}
		
		custEnquiry.setEnquirydate(date1);
		custEnquiry.setEnquirytime(time1);
		custEnquiry.setEmpemail(empemail);
		
		boolean status = custEnquiryService.addCustEnquiryDetailsService(custEnquiry);
		if(status)
		{
			redirectAttributes.addAttribute("redirect_success", "Customer enquiry details added successfully");
		}
		else
		{
			redirectAttributes.addAttribute("redirect_error", "Customer enquiry details not added due to some error");
		}
		
		return "redirect:/customerEnquiryPage";
	}
	
	@GetMapping("/customerFolloupPage")
	public String customerFolloupPage()
	{
		return "customer-followups";
	}
}
