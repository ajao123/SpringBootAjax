package com.allissonjardel.projeto.service;

import java.util.List;

import com.allissonjardel.projeto.model.Categoria;

public interface CategoriaService {

	void insert(Categoria entity);
	void update(Categoria entity, Long id);
	Categoria findById(Long id);
	List<Categoria> getAll();
	void delete(Long id);
	
}
