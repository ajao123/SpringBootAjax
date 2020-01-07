package com.allissonjardel.projeto.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.allissonjardel.projeto.model.Promocao;

public interface PromocaoService {

	void insert(Promocao entity);
	void update(Promocao entity, Long id);
	Promocao findById(Long id);
	Page<Promocao> getAllPageable(Pageable pageable);
	List<Promocao> getAll();
	Page<Promocao> findByPreco(BigDecimal preco, Pageable pageable);
	Page<Promocao> getAllDesc();
	Page<Promocao> getBySite(String site);
	List<String> getSiteByTermo(String termo);
	Page<Promocao> getCards(String site, Integer page);
	Integer addLike(Long id);
	Page<Promocao> findByTituloOrSiteOrCategoria(String search, Pageable pageable);
	void delete(Long id);
	
	
}
