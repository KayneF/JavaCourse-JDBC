package application;

import java.util.List;

import db.DB;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=== TEST 1: seller findById ===");
		Seller seller = sellerDao.findById(2);
		System.out.println(seller);
		
		System.out.println("\n=== TEST 2: seller findByDepartment ===");
		Department dp = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(dp);
		for (Seller s : list) {
			System.out.println(s);
		}
		
		System.out.println("\n=== TEST 3: seller findAll ===");
		list = sellerDao.findAll();
		for (Seller s : list) {
			System.out.println(s);
		}
		
//		System.out.println("\n=== TEST 4: seller insert ===");
//		Seller newSeller = new Seller(
//				null, "Gregory Howe", "greg@gmail.com", new Date(), 4000.0, dp);
//		sellerDao.insert(newSeller);
//		System.out.println("New seller inserted successfully. New Id: "
//				+ newSeller.getId());
		
//		System.out.println("\n=== TEST 5: seller update ===");
//		seller = sellerDao.findById(2);
//		seller.setName("Bruce Wayne");
//		seller.setEmail("bruce@gmail.com");
//		sellerDao.update(seller);
//		System.out.println("Seller updated! New seller information: ");
//		System.out.println(seller);
		
//		System.out.println("\n=== TEST 6: seller delete ===");
//		sellerDao.deleteById(2);
//		System.out.println("Seller deleted successfully!");
		
		DB.closeConnection();
	}
}
