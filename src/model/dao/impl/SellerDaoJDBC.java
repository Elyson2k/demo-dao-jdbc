package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {
		
		
	}

	@Override
	public void update(Seller obj) {
		
		
	}

	@Override
	public void deleteBy(Integer id) {
		
		
	}
	
	@Override
	public Seller findById(Integer id) { //Essa função procura o ID informado e retorna se ele existe na tabela.
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		// Selecionando as tabela na qual quer acessar.
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.id = ?");
			
			st.setInt(1, id); // Preenchendo o "?"
			rs = st.executeQuery(); // Executando o comando, e retornando uma tabela para o RS(ResultSet).
			if(rs.next()) { // rs pode retornar null e comeaça em 0, se ele retornar verdadeiro, entra no IF e significa que retornou a linha da tabela.
				Department dep = new Department(); // Criando um departamento temporario.
				dep.setId(rs.getInt("DepartmentId")); // Sentando os valores de departmentid.
				dep.setName(rs.getString("DepName")); // Setando os valores de depname.
				Seller obj = new Seller(); 
				obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("Name"));
				obj.setEmail(rs.getString("email"));
				obj.setBaseSalary(rs.getDouble("BaseSalary"));
				obj.setBirthDate(rs.getDate("BirthDate"));
				obj.setDepartment(dep);
				return obj;
			} else {
				return null;
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeConnection();
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Seller> findAll() {
		
		return null;
	}
	
}
