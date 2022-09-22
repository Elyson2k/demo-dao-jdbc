package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {
	
	// Interface que cumpre o contrato de inserir, deletar, atualizar e retornar todos os departamentos.
	void insert(Seller obj);
	void update(Seller obj);
	void deleteBy(Integer id);
	Seller findById(Integer id);
	List<Seller> findAll();
	
}
