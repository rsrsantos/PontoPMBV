package br.com.Core;

import br.com.DAO.JpaUtil;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.EntityManager;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AgendamentoJobElgin implements Job {

	EquipamentoBuscarConexaoElgin conectarRelogios = new EquipamentoBuscarConexaoElgin();

	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		EntityManager manager = JpaUtil.getEntityManager();
		
		System.out.println("... executando tarefa agendada para hora: " + (new Date()));

		Date hora = context.getFireTime();
		
		try {
			EquipamentoBuscarConexaoElgin.pegarEventosRelogio(manager, hora);
                        
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
