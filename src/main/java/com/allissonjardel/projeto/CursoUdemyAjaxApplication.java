package com.allissonjardel.projeto;

import java.util.TimeZone;

import org.directwebremoting.spring.DwrSpringServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import com.allissonjardel.projeto.service.impl.SocialMetaTagService;

@ImportResource(locations = "classpath:dwr-spring.xml")
@SpringBootApplication
public class CursoUdemyAjaxApplication{
	
	@Autowired
	SocialMetaTagService service;
	
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT-3"));
		SpringApplication.run(CursoUdemyAjaxApplication.class, args);
	}
	
	@Bean
	public ServletRegistrationBean<DwrSpringServlet> dwrSpringServlet(){
		DwrSpringServlet dwrServlet = new DwrSpringServlet();
		ServletRegistrationBean<DwrSpringServlet> registrationBean = 
				new ServletRegistrationBean<>(dwrServlet, "/dwr/*");
		
		registrationBean.addInitParameter("debug", "true");
		registrationBean.addInitParameter("activeReverseAjaxEnabled", "true");	
		return registrationBean;
	}
	
//	@Override
//	public void run(String... args) throws Exception {
//		SocialMetaTag tag =  service.getSocialMetaTagByUrl("https://www.udemy.com/course/spring-boot-mvc-com-thymeleaf/");
//		System.out.println(tag.toString());
//		
//		tag = service.getSocialMetaTagByUrl("https://www.pichau.com.br/processador-amd-ryzen-3-3200g-quad-core-3-6ghz-4ghz-turbo-6mb-cache-am4-yd3200c5fhbox");
//		System.out.println(tag.toString());
//	}

}





