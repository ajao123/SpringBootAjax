
var pageNumber = 0;

$(document).ready(function(){
	$("#loader-img").hide();
	$("#fim-btn").hide();

	 init();
	 teste();
});

// efeito infinite scroll
$(window).scroll(function(){
	
	var scrollTop = $(this).scrollTop();
	//Tamanho do documento - Tamanho da janela
	var conteudo = $(document).height() - $(window).height();
	
	console.log('scrollTop:', scrollTop, ' | ', 'conteudo:', conteudo, 
			' | ','Document.height:', $(document).height(), 
			' | ','Window.height:',  $(window).height(),  );
	
	if (scrollTop >= conteudo){
		pageNumber++;
		setTimeout(function(){
				loadByScrollBar();
				teste();
		}, 200);
	}
	
});

function loadByScrollBar(){
	var site = $("#autocomplete-input").val();
	
	$.ajax({
		method: "GET",
		url: "/promocao/list/ajax",
		data: {
			page: pageNumber,
			site: site
		},
		beforeSend: function(){
			$("#loader-img").show();
		},
		success: function( response ){
			//console.log("resposta > ", response);
			if(response.length > 150){
				$(".row").fadeIn(250, function(){
					$(this).append(response);
				});
			}else{
				$("#fim-btn").show();
				$("#loader-img").removeClass("loader");
			}
			
		},
		error: function(xhr){
			alert("Ops, ocorreu um erro: " + xhr.status + " - " + xhr.statusText);
		},
		complete: function(){
			$("#loader-img").hide();
		}
	});
}

//autocomplete
$("#autocomplete-input").autocomplete({
	source: function(request, response){
		$.ajax({
			method: "GET",
			url: "/promocao/site",
			data: {
				termo: request.term
			},
			success: function(result){
				response(result);
			}
		});
	}
});

$("#autocomplete-submit").on("click", function(){
	var site = $("#autocomplete-input").val();
	$.ajax({
		method: "GET",
		url: "/promocao/site/list",
		data: {
			site: site
		},
		beforeSend: function(){
			pageNumber = 0;
			$("#fim-btn").hide();
			$(".row").fadeOut(400, function(){
				$(this).empty();
			});
		},
		success: function(response){
			$(".row").fadeIn(250, function(){
				$(this).append(response);
			});
		}, 
		error: function(xhr){
			alert("Ops, algo deu errado: " + xhr.status + ", " + xhr.statusText);
		}
	});
});

// adicionar likes
// $("button[id*='likes-btn-']").on("click", function(){
$(document).on("click", "button[id*='likes-btn-']", function(){
	var id = $(this).attr("id").split("-")[2];
	console.log("id: ", id);
	
	$.ajax({
		method: "POST",
		url: "/promocao/like/" + id,
		success: function(response){
			console.log("response: ", response);
			$("#likes-count-"+id).text(response);
		},
		error: function(xhr){
			alert("Ops, ocorreu um erro: " + xhr.status + ", " + xhr.statusText);
		}
	});
	
});


function teste(){
	var aki = "aki";
	$.ajax({
		method: "POST",
		url: "/promocao/teste",
		// data: Valor a ser enviado na requisicao
		//data: aki, null
		data:{
			aki: aki,
		}
	
	});
}

// Ajax Reverse
var totalOfertas = 0;

$(document)

function init(){
	console.log("dwr init ...");
	
	dwr.engine.setActiveReverseAjax(true);
	dwr.engine.setErrorHandler(error);
	
	DWRAlertaPromocoes.init();
}

function error(exception){
	console.log("dwr error: ", exception);
}

function showButton(count){
	totalOfertas = totalOfertas + count;
	$("#btn-alert").show(function(){
		$(this)
			.attr("style", "display: block;")
			.text("Veja " + totalOfertas + " novas(s) oferta(s) !!!")
	});
	
}

$("#btn-alert").on("click", function(){
	$.ajax({
		method: "GET",
		url: "/promocao/list/ajax",
		data: {
			page: 0
		},
		beforeSend: function(){
			pageNumber = 0;
			totalOfertas = 0;
			$("#fim-btn").hide();
			$("#loader-img").addClass("loader");
			$("#btn-alert").attr("style", "display: none;");
			$(".row").fadeOut(400, function(){
				$(this).empty();
			});
		},
		success: function(response){
			$("#loader-img").removeClass("loader");
			$(".row").fadeIn(250, function(){
				$(this).append(response);
			});
		}, 
		error: function(xhr){
			alert("Ops, algo deu errado: " + xhr.status + ", " + xhr.statusText);
		}
	});
});





























