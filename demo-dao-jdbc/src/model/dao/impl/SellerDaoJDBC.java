package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DBException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection conn;
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		
		PreparedStatement pst = null;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT INTO seller ");
			sb.append("(Name, Email, BirthDate, BaseSalary, DepartmentId) ");
			sb.append("VALUES (?, ?, ?, ?, ?) ");
			pst = conn.prepareStatement(
					sb.toString(), Statement.RETURN_GENERATED_KEYS);
			
			pst.setString(1, obj.getName());
			pst.setString(2, obj.getEmail());
			pst.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			pst.setDouble(4, obj.getBaseSalary());
			pst.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = pst.executeUpdate();
			if (rowsAffected > 0) {
				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DBException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeStatement(pst);
		}
	}

	@Override
	public void update(Seller obj) {
		
		PreparedStatement pst = null;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("UPDATE seller ");
			sb.append("SET Name = ?, ");
			sb.append("Email = ?, ");
			sb.append("BirthDate = ?, ");
			sb.append("BaseSalary = ?, ");
			sb.append("DepartmentId = ? ");
			sb.append("WHERE Id = ? "); 
			pst = conn.prepareStatement(
					sb.toString(), Statement.RETURN_GENERATED_KEYS);
					
			pst.setString(1, obj.getName());
			pst.setString(2, obj.getEmail());
			pst.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			pst.setDouble(4, obj.getBaseSalary());
			pst.setInt(5, obj.getDepartment().getId());
			pst.setInt(6, obj.getId());
			
			pst.executeUpdate();
		}
		catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeStatement(pst);
		}
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(""
					+ "DELETE FROM seller WHERE Id = ? ");
			pst.setInt(1, id);
			int rows = pst.executeUpdate();
			if (rows == 0) {
				throw new DBException("There was no data attached to this Id.");
			}
		}
		catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeStatement(pst);
		}
	}

	@Override
	public Seller findById(Integer id) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT s.*, dp.Name as DepName ");
			sb.append("FROM seller s INNER JOIN department dp ");
			sb.append("ON s.DepartmentId = dp.Id ");
			sb.append("WHERE s.Id = ? ");
			pst = conn.prepareStatement(sb.toString());
			
			pst.setInt(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				Seller sel = instantiateSeller(rs, dep);
				return sel;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeStatement(pst);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
	public List<Seller> findByDepartment(Department dp) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT s.*, dp.Name as DepName ");
			sb.append("FROM seller s INNER JOIN department dp ");
			sb.append("ON s.DepartmentId = dp.Id ");
			sb.append("WHERE s.DepartmentId = ? ");
			sb.append("ORDER BY Name ");
			pst = conn.prepareStatement(sb.toString());
			
			pst.setInt(1, dp.getId());
			rs = pst.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			while (rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller sel = instantiateSeller(rs, dep);
				list.add(sel);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeStatement(pst);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findAll() {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT s.*, dp.Name as DepName ");
			sb.append("FROM seller s INNER JOIN department dp ");
			sb.append("ON s.DepartmentId = dp.Id ");
			sb.append("ORDER BY Name ");
			pst = conn.prepareStatement(sb.toString());
			
			rs = pst.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			while (rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller sel = instantiateSeller(rs, dep);
				list.add(sel);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeStatement(pst);
			DB.closeResultSet(rs);
		}
	}
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}
	
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		
		Seller sel = new Seller();
		sel.setId(rs.getInt("Id"));
		sel.setName(rs.getString("Name"));
		sel.setEmail(rs.getString("Email"));
		sel.setBaseSalary(rs.getDouble("BaseSalary"));
		sel.setBirthDate(rs.getDate("BirthDate"));
		sel.setDepartment(dep);
		return sel;
	}
}
