package com.allissonjardel.projeto.webcontroller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.allissonjardel.projeto.model.Categoria;
import com.allissonjardel.projeto.model.Promocao;
import com.allissonjardel.projeto.model.dto.PromocaoDTO;
import com.allissonjardel.projeto.service.CategoriaService;
import com.allissonjardel.projeto.service.PromocaoService;
import com.allissonjardel.projeto.service.impl.PromocaoDataTableServiceImpl;
import com.allissonjardel.projeto.service.impl.SocialMetaTagService;

@Controller
@RequestMapping("/promocao")
public class PromocaoController {

	private static Logger log = LoggerFactory.getLogger(SocialMetaTagService.class);
	
	@Autowired
	private PromocaoService service;
	
	@Autowired	
	private CategoriaService categoriaService;
	
	
	@GetMapping("/table")
	public String showTabela() {
		return "promo-datatables";
	}
	
	@GetMapping("/datatables/server")
	public ResponseEntity<?> datatables(HttpServletRequest request){
		Map<String, Object> data = new PromocaoDataTableServiceImpl().execute(service, request);
		return ResponseEntity.ok(data);
	}
	
	@GetMapping("/list")
	public String listarOfertas(ModelMap model) {
			
		model.addAttribute("promocoes", service.getAllDesc());
		return "promo-list";
	}
	
	@GetMapping("/list/ajax")
	public String listarCards(@RequestParam(name="page", defaultValue = "1") int page, 
							  @RequestParam(name="site", defaultValue = "") String site, 
							  ModelMap model) {
		
		model.addAttribute("promocoes", service.getCards(site, page));	
		return "promo-card";
	}
	
	@ModelAttribute("categorias")
	public List<Categoria> getCategorias(){
		return categoriaService.getAll();
	}
	
	@GetMapping("/add")
	public String abrirCadastro() {	
		return "promo-add";
	}

	@PostMapping("/save")
	public ResponseEntity<?> salvarPromocao(@Valid Promocao entity, BindingResult result) {	
		
		if(result.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			for(FieldError error : result.getFieldErrors()) {
				errors.put(error.getField(), error.getDefaultMessage());
			}
			
			return ResponseEntity.unprocessableEntity().body(errors);
		}
		
		log.info("Promocao {}", entity.toString());	
		entity.setDtCadastro(LocalDateTime.now());
		service.insert(entity);
		
		return ResponseEntity.ok().build();
		
	}
	
	@PostMapping("/like/{id}")
	public ResponseEntity<?> adicionarLikes(@PathVariable("id") Long id){
		return ResponseEntity.ok(service.addLike(id));
	}
	
	@GetMapping("/edit/{id}")
	public ResponseEntity<?> preEditarPromocao(@PathVariable("id") Long id){
		return ResponseEntity.ok(service.findById(id) );
	}
	
	@PostMapping("/edit")
	public ResponseEntity<?> editarPromocao(@Valid PromocaoDTO promocaoDTO, BindingResult result){
		
		if(result.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			for(FieldError error : result.getFieldErrors()) {
				errors.put(error.getField(), error.getDefaultMessage());
			}
			
			return ResponseEntity.unprocessableEntity().body(errors);
		}
		
		Promocao promocao = service.findById(promocaoDTO.getId());
		promocao.setTitulo(promocaoDTO.getTitulo());
		promocao.setPreco(promocaoDTO.getPreco());
		promocao.setCategoria(promocaoDTO.getCategoria());
		promocao.setLinkImagem(promocaoDTO.getLinkImagem());
		promocao.setDescricao(promocaoDTO.getDescricao());
		
		service.insert(promocao);
		return ResponseEntity.ok().build();
	}
	
	
	
	@GetMapping("/delete/{id}")
	public ResponseEntity<?> deletarPromocao(@PathVariable("id") Long id){
		service.delete(id);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/site")
	public ResponseEntity<?> getSitesByTermo(@RequestParam(name="termo") String termo){
		return ResponseEntity.ok(service.getSiteByTermo(termo));
	}
	@GetMapping("/site/list")
	public String getPromocaoBySite(@RequestParam(name="site") String site, ModelMap model){
		
		model.addAttribute("promocoes", service.getBySite(site));	
		return "promo-card";
	}
	
	@PostMapping("/teste")
	public ResponseEntity<?> teste(String entity){
		
		System.out.println(entity + " poxa vida");
		return ResponseEntity.ok().build();
	}
	
}
