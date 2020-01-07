package com.allissonjardel.projeto.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.allissonjardel.projeto.model.Promocao;
import com.allissonjardel.projeto.repository.PromocaoRepository;
import com.allissonjardel.projeto.service.PromocaoService;

@Service
public class PromocaoServiceImpl implements PromocaoService{

	@Autowired
	PromocaoRepository repository;
	
	@Override
	public void insert(Promocao entity) {
		repository.save(entity);
	}

	@Override
	public void update(Promocao entity, Long id) {
		Promocao entity2 =  repository.findById(id).get();
		BeanUtils.copyProperties(entity, entity2, "id");
		repository.save(entity2);
	}

	@Override
	public Promocao findById(Long id) {
		return repository.findById(id).get();
	}

	@Override
	public List<Promocao> getAll() {		
		return repository.findAll();
	}
	
	@Override
	public Page<Promocao> findByPreco(BigDecimal preco, Pageable pageable) {		
		return repository.findByPreco(preco, pageable);
	}

	@Override
	public Page<Promocao> getAllPageable(Pageable pageable) {		
		return repository.findAll(pageable);
	}
	
	@Override
	public Page<Promocao> getAllDesc() {
		PageRequest pageRequest = PageRequest.of(0, 8);
		return repository.getPromocaoByDataDesc(pageRequest);
	}
	
	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	public Page<Promocao> getBySite(String site) {
		PageRequest pageRequest = PageRequest.of(0, 8);
		return repository.getPromocaoBySite(site, pageRequest);
	}

	@Override
	public List<String> getSiteByTermo(String site) {
		return repository.getSitesByTermo(site);
	}

	@Override
	public Page<Promocao> getCards(String site, Integer page) {
		
		PageRequest pageRequest = PageRequest.of(page, 8);
		
		if(site.isEmpty()) {
			return repository.getPromocaoByDataDesc(pageRequest);	
		}else {
			return repository.getPromocaoBySite(site, pageRequest);	
		}
		
	}

	@Override
	public Integer addLike(Long id) {
		repository.updateAdicionarLike(id);
		return repository.findLikesById(id);
	}

	@Override
	public Page<Promocao> findByTituloOrSiteOrCategoria(String search, Pageable pageable) {
		return repository.findByTituloOrSiteOrCategoria(search, pageable);
	}

}
