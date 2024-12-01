package in.sp.main.service;

import java.util.List;

import in.sp.main.entity.Product;

public interface ProductService
{
	public boolean addProductService(Product product);
	public List<Product> getAllProductsListService();
	public Product getProductDetailsService(String coursename);
	public List<String> getAllCourseNameService();
}
