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
				
				/* Quando programamos orientado a objeto, mesmo que procurando em forma de tabela, temos que criar 
				 * os objetos assossiados instanciados na memoria, como esta abaio, pois so buscando como esta a acima
				 * não funciona */
				
				Department dep = instantiateDepartment(rs);// Criando um departamento temporario.
				Seller obj = instantiateSeller(rs, dep); 
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

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("id")); // Quem vai ser o meu sellerId? vai ser o ID que esta la na tabela, para você pegar ele, você pega o resultado,passa o tipo e dentro o nome do campo.
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
		
		return null;
	}
	
}
