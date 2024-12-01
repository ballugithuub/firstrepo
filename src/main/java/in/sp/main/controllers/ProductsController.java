package in.sp.main.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.sp.main.entity.Product;
import in.sp.main.service.ProductService;
import in.sp.main.urls.OtherUrls;

@Controller
public class ProductsController
{
	@Autowired
	ProductService productService;
	
	@GetMapping("/addProducts")
	public String openAddProductsPage(
			Model model,
			@RequestParam(name="redirect_attr_success", required = false) String success,
			@RequestParam(name="redirect_attr_error", required = false) String error)
	{
		model.addAttribute("productAttr", new Product());
		
		model.addAttribute("model_success", success);
		model.addAttribute("model_error", error);
		
		return "add-products";
	}
	
	@PostMapping("/addCourseForm")
	public String addCourseDetails(
			@ModelAttribute("productAttr") Product product,
			@RequestParam("courseimage") MultipartFile courseImage,
			@RequestParam("trainersimage") MultipartFile trainerImage,
			RedirectAttributes redirectAttributes
			)
	{
		boolean status1 = saveImage(courseImage);
		if(!status1)
		{
			System.out.println("course image not uploaded due to some error");
		}
		boolean status2 = saveImage(trainerImage);
		if(!status2)
		{
			System.out.println("trainers image not uploaded due to some error");
		}
		
		boolean status = productService.addProductService(product);
		if(status)
		{
			redirectAttributes.addAttribute("redirect_attr_success", "Product added successfully");
		}
		else
		{
			redirectAttributes.addAttribute("redirect_attr_error", "Product not added due to some error");
		}
		
		return "redirect:/addProducts";
	}
	
	private boolean saveImage(MultipartFile file)
	{
		boolean status = false;
		
		try
		{
			String fileName = file.getOriginalFilename();
			Path filePath = Paths.get(OtherUrls.IMAGE_UPLOAD_FOLDER, fileName);
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			
			status = true;
		}
		catch(Exception e)
		{
			status = false;
			e.printStackTrace();
		}
		
		return status;
	}
	
	@GetMapping("/productsList")
	public String openProductsListPage(Model model)
	{
		List<Product> list_products = productService.getAllProductsListService();
		model.addAttribute("model_products_list", list_products);
		return "products-list";
	}
	
	@GetMapping("/courseDetails")
	public String openCourseDetailsPage(@RequestParam("courseName") String coursename, Model model)
	{
		Product product = productService.getProductDetailsService(coursename);
		model.addAttribute("model_product", product);
		
		return "course-details";
	}
}
