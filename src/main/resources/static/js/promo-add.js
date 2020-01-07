//submit do formulario para o controller
$("#form-add-promo").submit(function(evt){
	//bloquear o comportamento padrão do submit
	evt.preventDefault();
	
	var promo = {};
	promo.linkPromocao = $("#linkPromocao").val();
	promo.descricao = $("#descricao").val();
	promo.preco = $("#preco").val();
	promo.titulo = $("#titulo").val();
	promo.categoria = $("#categoria").val();
	promo.linkImagem = $("#linkImagem").attr("src");
	promo.site = $("#site").text();
	promo.likes = 0;
	
	console.log('promo > ', promo);
	
	$.ajax({
		method: "POST",
		url: "/promocao/save",
		// data: Valor a ser enviado na requisicao
		data: promo,
		beforeSend: function(){
			
			//removendo as mensagens
			$("span").closest('.error-span').remove();
			
			//remover as bordas vermelhas
			$(".is-invalid").removeClass("is-invalid");
//			$("#categoria").removeClass("is-invalid");
//			$("#preco").removeClass("is-invalid");
//			$("#linkPromocao").removeClass("is-invalid");
//			$("#titulo").removeClass("is-invalid");
//			
			//habilita o loanding
			$("#form-add-promo").hide();
			$("#loader-form").addClass("loader").show();
		},
		success: function(){
			$("#form-add-promo").each(function(){
				this.reset();
			});
			$("#linkImagem").attr("src", "/images/promo-dark.png");
			$("site").text("");
			$("#alert").
			removeClass("alert alert-danger").
			addClass("alert alert-success").
			text("OK !!! Promoção cadastrada com sucesso.");
		},
		statusCode:{
			422: function(xhr){
				console.log('status error:', xhr.status);
				var errors = $.parseJSON(xhr.responseText);
				$.each(errors, function(key, val){
					$("#" + key).addClass("is-invalid");
					$("#error-" + key).
					addClass("invalid-feedback").
					append("<span class='error-span'>" + val + "</span>");
				});
				
			}
		},
		error: function(xhr){
			console.log("> error: ", xhr.responseText);
			$("#alert").addClass("alert alert-danger").text("Não foi possível salvar esta promoção.");
		},
		complete: function(){
			$("#loader-form").fadeOut(800, function(){
				$("#form-add-promo").fadeIn(250);
				$("#loader-form").removeClass("loader");
			});
		}
	});
	
});


//funcao para capturar as meta tags
$("#linkPromocao").on('change', function(){
	
	var url = $(this).val();
	
	if(url.length > 7){
		
		$.ajax({
			method:"POST",
			url: "/meta/info?url=" + url,
			cache: false,
			beforeSend: function(){
				$("#alert").removeClass("alert alert-danger alert-success").text('');
				$("#titulo").val("");
				$("#site").text("");
				$("#linkImagem").attr("src", "");
				$("#loader-img").addClass("loader");
				
			},
			success: function( response ){
				console.log(response);
				$("#titulo").val(response.title);
				$("#site").text(response.site.replace("@",""));
				$("#linkImagem").attr("src", response.image);
			},
			statusCode:{
				404: function(){
					$("#alert").addClass("alert alert-danger").text("Nenhuma informação pode ser recuperada dessa url");
					$("#linkImagem").attr("src", "/images/promo-dark.png");
				},
				500: function(){
					$("#alert").addClass("alert alert-danger").text("Ocorreu um erro no sistema. Tente mais tarde");
					$("#linkImagem").attr("src", "/images/promo-dark.png");
				}
			},
			complete: function(){
				$("#loader-img").removeClass("loader");
			}
		});	
	}
});