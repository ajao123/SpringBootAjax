package com.allissonjardel.projeto.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allissonjardel.projeto.model.Categoria;
import com.allissonjardel.projeto.repository.CategoriaRepository;
import com.allissonjardel.projeto.service.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService{

	@Autowired
	CategoriaRepository repository;
	
	@Override
	public void insert(Categoria entity) {
		repository.save(entity);
	}

	@Override
	public void update(Categoria entity, Long id) {
		Categoria entity2 =  repository.findById(id).get();
		BeanUtils.copyProperties(entity, entity2, "id");
		repository.save(entity2);
	}

	@Override
	public Categoria findById(Long id) {
		return repository.findById(id).get();
	}

	@Override
	public List<Categoria> getAll() {
		return repository.findAll();
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}

}
