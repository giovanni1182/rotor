package com.gio.crm.model;

import infra.model.Home;
import infra.sql.SQLQuery;

public class CodificacoesHomeImpl extends Home implements CodificacoesHome
{

	public CodificacaoCobertura obterCodificacaoCobertura(String numeroCobertura, CodificacaoPlano plano) throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		CodificacaoCobertura cobertura = null;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,codificacoes where evento.id = codificacoes.id and classe='CodificacaoCobertura' and codigo = ? and superior = ?");
		query.addString(numeroCobertura);
		query.addLong(plano.obterId());
		
		if(query.execute().length > 0)
		{
			long id = query.executeAndGetFirstRow().getLong("id");
			
			cobertura = (CodificacaoCobertura) home.obterEventoPorId(id);
		}
		
		return cobertura;
	}

	public CodificacaoDetalhe obterCodificacaoDetalhe(String numeroDetalhe, CodificacaoRisco risco) throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		CodificacaoDetalhe detalhe = null;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,codificacoes where evento.id = codificacoes.id and classe='CodificacaoDetalhe' and codigo = ? and superior = ?");
		query.addString(numeroDetalhe);
		query.addLong(risco.obterId());
		
		if(query.execute().length > 0)
		{
			long id = query.executeAndGetFirstRow().getLong("id");
			
			detalhe = (CodificacaoDetalhe) home.obterEventoPorId(id);
		}
		
		return detalhe;
	}

	public CodificacaoPlano obterCodificacaoPlano(String numeroPlano) throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		CodificacaoPlano plano = null;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,codificacoes where evento.id = codificacoes.id and classe='CodificacaoPlano' and codigo = ?");
		query.addString(numeroPlano);
		
		if(query.execute().length > 0)
		{
			long id = query.executeAndGetFirstRow().getLong("id");
			
			plano = (CodificacaoPlano) home.obterEventoPorId(id);
		}
		
		return plano;
	}

	public CodificacaoRisco obterCodificacaoRisco(String numeroRisco, CodificacaoCobertura cobertura) throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		CodificacaoRisco risco = null;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,codificacoes where evento.id = codificacoes.id and classe='CodificacaoRisco' and codigo = ? and superior = ?");
		query.addString(numeroRisco);
		query.addLong(cobertura.obterId());
		
		if(query.execute().length > 0)
		{
			long id = query.executeAndGetFirstRow().getLong("id");
			
			risco = (CodificacaoRisco) home.obterEventoPorId(id);
		}
		
		return risco;
	}

}
