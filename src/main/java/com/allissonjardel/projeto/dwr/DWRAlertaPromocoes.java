package com.allissonjardel.projeto.dwr;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.allissonjardel.projeto.repository.PromocaoRepository;

@Component
@RemoteProxy
public class DWRAlertaPromocoes {

	@Autowired
	private PromocaoRepository repository;
	
	private Timer timer;

	private LocalDateTime getDtCadastroByUltimaPromocao() {
		PageRequest pageRequest = PageRequest.of(0,1,Direction.DESC, "dt_cadastro");
		return repository.findUltimaDataDePromocao(pageRequest).getContent().get(0).toLocalDateTime();
	}
	
	@RemoteMethod
	public synchronized void init() {
		
		System.out.println("DWR está ativado!");
		
		LocalDateTime lastDate =  getDtCadastroByUltimaPromocao();
		System.out.println(lastDate);
		
		WebContext context = WebContextFactory.get();
		
		timer = new Timer();
		timer.schedule(new AlertTask(context, lastDate), 10000, 60000);
		
	}
	
	class AlertTask extends TimerTask{
		
		private LocalDateTime lastDate;
		private WebContext context;
		private Long count;
		
		public AlertTask(WebContext context, LocalDateTime lastDate) {
			super();
			this.lastDate = lastDate;
			this.context = context;
		}

		@Override
		public void run() {
			
			String session = context.getScriptSession().getId();
			
			Browser.withSession(context, session, new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Map<String, Object> map = repository.TotalAndUltimaPromocaoByDtCadastro(lastDate);
					System.out.println(map);
			
					count = ( (BigInteger) map.get("count") ).longValue();
					
					lastDate = (map.get("lastDate") == null) ? lastDate : ((Timestamp) map.get("lastDate")).toLocalDateTime();
					
					Calendar time = Calendar.getInstance();
					time.setTimeInMillis(context.getScriptSession().getLastAccessedTime());
					
					System.out.println("count: " + count
									   +", lastDate: " + lastDate
									   +"<" + session + "> " + " <" + time.getTime() + "");
					
					if(count > 0) {
						ScriptSessions.addFunctionCall("showButton", count);
					}
				}
				
			});

		}
		
	}
	
}






















