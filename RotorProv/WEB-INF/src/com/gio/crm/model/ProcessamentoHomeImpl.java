package com.gio.crm.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import infra.model.Home;
import infra.sql.SQLQuery;

public class ProcessamentoHomeImpl extends Home implements ProcessamentoHome
{
	public Processamento obterProcessamentoDoDia(String tipo) throws Exception
	{
		Processamento processamento = null;
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String dataAtual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,processamento where evento.id = processamento.id and data = ? and tipo = ?");
		query.addString(dataAtual);
		query.addString(tipo);
		
		long id = query.executeAndGetFirstRow().getLong("id");
		
		if(id > 0)
			processamento = (Processamento) home.obterEventoPorId(id);
		
		return processamento;
	}
}