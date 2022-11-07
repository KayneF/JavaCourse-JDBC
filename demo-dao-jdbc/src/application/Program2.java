package application;

import java.util.ArrayList;
import java.util.List;

import db.DB;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		
		DepartmentDao depDao = DaoFactory.createDepartmentDao();
		List<Department> list = new ArrayList<>();
		
		// LIST ALL
		list = depDao.findAll();
		for (Department dp : list) {
			System.out.println(dp);
		}
		
		// FIND BY ID
		Department dep = depDao.findById(1);
		System.out.println(dep);
		
		// INSERT DEPARTMENT
//		dep = new Department(7, "DP1");
//		depDao.insert(dep);
		
		// UPDATE DEPARTMENT
//		dep = new Department(7, "DP2");
//		depDao.update(dep);
		
		// DELETE DEPARTMENT
//		depDao.deleteById(7);
		
		
		
		DB.closeConnection();
	}
}
