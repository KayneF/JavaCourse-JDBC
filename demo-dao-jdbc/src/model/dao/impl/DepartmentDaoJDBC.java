package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DBException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection conn;
	public DepartmentDaoJDBC(Connection connection) {
		this.conn = connection;
	}

	@Override
	public void insert(Department dep) {
		PreparedStatement pst = null;
		try {
			String query = "INSERT INTO department (Name) VALUES (?) ";
			pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, dep.getName());
			
			int rowsAffected = pst.executeUpdate();
			if (rowsAffected > 0) {
				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					dep.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DBException("No rows where affected.");
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
	public void update(Department dep) {
		PreparedStatement pst = null;
		try {
			String query = "UPDATE department SET Name = ? WHERE Id = ? ";
			pst = conn.prepareStatement(query);
			pst.setString(1, dep.getName());
			pst.setInt(2, dep.getId());
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
			String query = "DELETE FROM department WHERE Id = ? ";
			pst = conn.prepareStatement(query);
			pst.setInt(1, id);
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
	public Department findById(Integer id) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM department WHERE Id = ? ";
			pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				Department dep = new Department();
				dep.setId(rs.getInt("Id"));
				dep.setName(rs.getString("Name"));
				return dep;
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
	public List<Department> findAll() {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM department ORDER BY Name ";
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			
			List<Department> list = new ArrayList<>();
			while (rs.next()) {
				Department dep = new Department();
				dep.setId(rs.getInt("Id"));
				dep.setName(rs.getString("Name"));
				list.add(dep);
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
}
