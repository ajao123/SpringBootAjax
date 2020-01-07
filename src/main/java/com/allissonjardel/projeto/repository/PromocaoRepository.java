package com.allissonjardel.projeto.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.allissonjardel.projeto.model.Promocao;

public interface PromocaoRepository extends JpaRepository<Promocao, Long>{
	
	@Query(value = "SELECT * FROM PROMOCAO ORDER BY DT_CADASTRO DESC", nativeQuery = true)
	Page<Promocao> getPromocaoByDataDesc(PageRequest pageRequest);
	
	@Query(value = "select count(p.id) as count, max(p.dt_cadastro) as lastDate "
			+ "from Promocao p where p.dt_cadastro > ?1", nativeQuery = true)
	Map<String, Object> TotalAndUltimaPromocaoByDtCadastro(LocalDateTime data);
	
	@Query(value = "SELECT P.DT_CADASTRO FROM PROMOCAO P", nativeQuery = true)
	Page<Timestamp> findUltimaDataDePromocao(Pageable pageable);
	
	@Query(value = "SELECT * FROM PROMOCAO WHERE PRECO = ?1 ORDER BY DT_CADASTRO", nativeQuery = true)
	Page<Promocao> findByPreco(BigDecimal preco, Pageable pageable);
	
	@Query(value = "SELECT * FROM PROMOCAO P INNER JOIN CATEGORIA C ON C.ID = P.CATEGORIA_ID "
			     + " WHERE P.SITE LIKE CONCAT('%', ?1, '%')"
				 + " OR P.TITULO LIKE CONCAT('%', ?1, '%') "
				 + " OR C.TITULO LIKE CONCAT('%', ?1, '%')  "		
				 , nativeQuery = true)
	Page<Promocao> findByTituloOrSiteOrCategoria(String search, Pageable pageable);
	
	@Query(value = "SELECT P.LIKES FROM PROMOCAO P WHERE P.ID = ?1", nativeQuery = true)
	Integer findLikesById(Long id);
	
	@Transactional(readOnly = false)
	@Modifying
	@Query(value = "UPDATE PROMOCAO P SET P.LIKES = P.LIKES + 1 WHERE P.ID = ?1", nativeQuery = true)
	void updateAdicionarLike(Long id);
	
	@Query(value = "SELECT DISTINCT P.SITE FROM PROMOCAO P WHERE P.SITE LIKE CONCAT('%', ?1, '%') ", nativeQuery = true)
	List<String> getSitesByTermo(String site);	
	
	@Query(value = "SELECT * FROM PROMOCAO WHERE SITE = ?1 ORDER BY DT_CADASTRO DESC", nativeQuery = true)
	Page<Promocao> getPromocaoBySite(String site, PageRequest pageRequest);	
	
}
