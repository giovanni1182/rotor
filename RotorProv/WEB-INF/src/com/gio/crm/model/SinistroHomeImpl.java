package com.gio.crm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class SinistroHomeImpl extends Home implements SinistroHome 
{
	public Sinistro obterSinistro(String numero) throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,sinistro where evento.id = sinistro.id and numero=?");
		query.addString(numero);

		long id = query.executeAndGetFirstRow().getLong("id");
		Sinistro sinistro = null;

		if (id > 0) 
		{
			EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
			
			sinistro = (Sinistro) home.obterEventoPorId(id);
		}

		return sinistro;
	}
	
	public Collection obterAseguradorasCentralRisco(String nomeAsegurado) throws Exception
	{
		Map sinistros = new TreeMap();
		
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select origem from evento,apolice,sinistro where evento.id = sinistro.id and evento.superior = apolice.id and nome_asegurado = ? group by origem");
		query.addString(nomeAsegurado);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("origem");
			
			Entidade entidade = home.obterEntidadePorId(id);
			
			sinistros.put(new Long(id), entidade);
		}
		
		return sinistros.values();
	}
	
	public void atualizarDatas() throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select evento.id from evento,sinistro where evento.id = sinistro.id");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Sinistro sinistro = (Sinistro) home.obterEventoPorId(id);
			
			Apolice apolice = (Apolice) sinistro.obterSuperior();
			
			sinistro.atualizarDataPrevistaInicio(apolice.obterDataPrevistaInicio());
			sinistro.atualizarDataPrevistaConclusao(apolice.obterDataPrevistaConclusao());
		}
	}
	
	public void modificarAfetadoPorSinistro() throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select evento.id from evento,sinistro where evento.id = sinistro.id");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Sinistro sinistro = (Sinistro) home.obterEventoPorId(id);
			
			Apolice apolice = (Apolice) sinistro.obterSuperior();
			
			apolice.atualizarAfetadoPorSinistro("Sí");
		}
	}
	
	public void manutSinistro() throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		EntidadeHome entidadehome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		ApoliceHome apolicehome = (ApoliceHome) this.getModelManager().getHome("ApoliceHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "SELECT evento.id,titulo,origem FROM sinistro,evento where evento.id = sinistro.id and superior = 0");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Sinistro sinistro = (Sinistro) home.obterEventoPorId(id);
			
			String titulo = rows[i].getString("titulo");
			
			String[] numero = titulo.split(":");
			
			String numero2 = numero[1].trim();
			
			//System.out.println(numero2);
			
			long origem = rows[i].getLong("origem");
			
			Entidade e = entidadehome.obterEntidadePorId(origem);
			
			Apolice ap = apolicehome.obterApolice(e, numero2);
			
			if(ap!=null)
				sinistro.atualizarSuperior(ap);
			else
				System.out.println("Não achou Apolice: " + numero2);
		}
	}
	
	public Collection<Sinistro> obterSinistros(Aseguradora aseguradora, String secao, String situacao, Date dataInicio, Date dataFim, String nomeAsegurado, String situacaoSinistro) throws Exception
	{
		Map<String, Sinistro> sinistros = new TreeMap<String, Sinistro>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		//String sql = "select evento.id from evento,sinistro,apolice,plano where evento.id = sinistro.id and superior = apolice.id and apolice.plano = plano.id";
		String sql = "select evento.id from evento,sinistro,apolice where evento.id = sinistro.id and superior = apolice.id";
		
		if(aseguradora!=null)
			sql+=" and origem = " + aseguradora.obterId();
		if(!situacao.equals("0"))
			sql+=" and apolice.situacao_seguro = '" + situacao +"'";
		if(dataInicio!=null)
			sql+=" and apolice.data_emissao>= " + dataInicio.getTime();
		if(dataFim!=null)
			sql+=" and apolice.data_emissao<= " + dataFim.getTime();
		if(!nomeAsegurado.equals(""))
			sql+=" and nome_asegurado like '%" + nomeAsegurado +"%'";
		if(!situacaoSinistro.equals("0"))
			sql+=" and sinistro.situacao= '" + situacaoSinistro+"'";
		
		//System.out.println(sql);
		
		//DEIXAR ESSA ORDENAÇÃO POR CAUSA DAS DUPLICIDADES
		sql+=" order by criacao DESC";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Sinistro sinistro = (Sinistro) home.obterEventoPorId(id);
			if(!secao.equals(""))
			{
				Apolice apolice = (Apolice) sinistro.obterSuperior();
				Plano plano = apolice.obterPlano();
				if(plano!=null)
				{
					if(plano.obterSecao().equals(secao))
						sinistros.put(sinistro.obterNumero()+i, sinistro);
				}
			}
			else
				sinistros.put(sinistro.obterNumero()+i, sinistro);
		}
		return sinistros.values();
	}

	public Collection<Sinistro> obterMaiores(Aseguradora aseguradora, String tipoPessoa, Date dataInicio, Date dataFim, int qtde, String situacao, String secao, double monto, String modalidade, String tipoInstrumento) throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		Collection<Sinistro> sinistros = new ArrayList<Sinistro>();
		
		String sql = "select TOP "+qtde+" evento.id from evento,sinistro,apolice,plano where evento.id = sinistro.id and superior = apolice.id and apolice.plano = plano.id";
		
		if(aseguradora!=null)
			sql+=" and origem = " + aseguradora.obterId();
		if(!tipoPessoa.equals("0"))
			sql+=" and tipo_pessoa = '" + tipoPessoa + "'";
		if(dataInicio!=null)
			sql+=" and data_sinistro>= " + dataInicio.getTime();
		if(dataFim!=null)
			sql+=" and data_sinistro<= " + dataFim.getTime();
		if(!situacao.equals("0"))
			sql+=" and situacao_seguro='"+situacao+"'";
		if(!secao.equals(""))
			sql+=" and plano.secao = '" + secao + "'";
		if(!modalidade.equals(""))
			sql+=" and plano.plano = '" + modalidade + "'";
		if(!tipoInstrumento.equals("0"))
			sql+=" and status_apolice = '" + tipoInstrumento + "'";
		if(monto>0)
			sql+=" and montante_gs>=" + monto;
		
		sql+= " ORDER BY montante_gs DESC";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Sinistro sinistro = (Sinistro) home.obterEventoPorId(id);
			
			sinistros.add(sinistro);
		}
		
		return sinistros;
	}
	
	public Sinistro obterSinistro(Entidade aseguradora, String numero) throws Exception
    {
    	SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterSinistro "+aseguradora.obterId()+",'"+numero+"'");
        
        long id = query.executeAndGetFirstRow().getLong("id");
        Sinistro sinistro = null;
        if(id > 0)
        {
            EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
            sinistro = (Sinistro)home.obterEventoPorId(id);
        }
        return sinistro;
    }
}