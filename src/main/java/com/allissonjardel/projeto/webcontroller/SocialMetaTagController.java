package com.allissonjardel.projeto.webcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.allissonjardel.projeto.model.SocialMetaTag;
import com.allissonjardel.projeto.service.impl.SocialMetaTagService;

@Controller
@RequestMapping("/meta")
public class SocialMetaTagController {

	@Autowired
	private SocialMetaTagService service;
	
	@PostMapping("/info")
	public ResponseEntity<SocialMetaTag> getDadosViaUrl(@RequestParam("url") String url){
		SocialMetaTag tag = service.getSocialMetaTagByUrl(url);
		return tag != null ? ResponseEntity.ok(tag) : ResponseEntity.notFound().build();
		
	}
	
}





















