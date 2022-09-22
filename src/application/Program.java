package application;

import java.util.Date;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Department dp = new Department(1,"A");
		System.out.println(dp);
		
		Seller sl = new Seller(1,"Elyson", "Elyson@Gmail.com", new Date(), 3000.0, dp);
		System.out.println(sl);
		
		SellerDao sellerDao = DaoFactory.createSellerDao();

	}

}
