package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import db.DB;
import db.DbException;
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
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) "
									 + "VALUES (? , ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rows = st.executeUpdate();
			
			if(rows>0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected.");
			}
		} catch(SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?");

				st.setString(1, obj.getName());
				st.setString(2, obj.getEmail());
				st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
				st.setDouble(4, obj.getBaseSalary());
				st.setInt(5, obj.getDepartment().getId());
				st.setInt(6, obj.getId());
				
				st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteBy(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM seller WHERE id = ?");
			st.setInt(1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Seller findById(Integer id) { // Essa função procura o ID informado e retorna se ele existe na tabela.

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
			if (rs.next()) { // rs pode retornar null e comeaça em 0, se ele retornar verdadeiro, entra no IF e significa que retornou a linha da tabela.
								

				/*
				 * Quando programamos orientado a objeto, mesmo que procurando em forma de
				 * tabela, temos que criar os objetos assossiados instanciados na memoria, como
				 * esta abaio, pois so buscando como esta a acima não funciona
				 */

				Department dep = instantiateDepartment(rs); 
				Seller obj = instantiateSeller(rs, dep);
				return obj;
			} else {
				return null;
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("id")); // Quem vai ser o meu sellerId? vai ser o ID que esta la na tabela, para você											// pegar ele, você pega o resultado,passa o tipo e dentro o nome do campo.
		obj.setName(rs.getString("Name")); // Os campos abaixo é a mesma logica para o campo acima.
		obj.setEmail(rs.getString("email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId")); // Quem vai ser o meu departamentoId? vai ser o departmentid que esta la na tabela, para você pegar ele, você pega o resultado,passa o tipo e dentro o nome do campo.
											
		dep.setName(rs.getString("DepName")); // Os campos abaixo é a mesma logica para o campo acima.
		return dep;
	}

	@Override
	public List<Seller> findAll() {

		PreparedStatement st = null;
		ResultSet rs = null;

		// Selecionando as tabela na qual quer acessar.
		try {
			st = conn.prepareStatement(
							"SELECT seller.*, department.Name as DepName "
							+ "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id "
							+ "ORDER BY Name");

			rs = st.executeQuery(); // Executando o comando, e retornando uma tabela para o RS(ResultSet).

			List<Seller> list = new ArrayList<>();

			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) { // rs pode retornar null e comeaça em 0, se ele retornar verdadeiro, entra no IF e significa que retornou a linha da tabela.
								 

				/*
				 * Quando programamos orientado a objeto, mesmo que procurando em forma de
				 * tabela, temos que criar os objetos assossiados instanciados na memoria, como
				 * esta abaixo, pois so buscando como esta a acima não funciona
				 */

				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}

			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department deparment) {

		PreparedStatement st = null;
		ResultSet rs = null;

		// Selecionando as tabela na qual quer acessar.
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
							+ "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id "
							+ "WHERE departmentId = ? "
							+ "ORDER BY Name");

			st.setInt(1, deparment.getId()); // Preenchendo o "?"
			rs = st.executeQuery(); // Executando o comando, e retornando uma tabela para o RS(ResultSet).

			List<Seller> list = new ArrayList<>();
			Set<Department> set = new HashSet<>();
			while (rs.next()) { // rs pode retornar null e comeaça em 0, se ele retornar verdadeiro, entra no IF e significa que retornou a linha da tabela.	
				
				/*
				 * Quando programamos orientado a objeto, mesmo que procurando em forma de
				 * tabela, temos que criar os objetos assossiados instanciados na memoria, como
				 * esta abaio, pois so buscando como esta a acima não funciona
				 */
				Department dep = instantiateDepartment(rs);
				if (dep == null) {
					dep = instantiateDepartment(rs);
					set.add(dep);
				}

				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}

			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			//DB.closeConnection();
		}

	}

}
