package com.gio.crm.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class ProcessamentoImpl extends EventoImpl implements Processamento
{
	private Date data;
	
	public class AgendaImpl implements Agenda
	{
		private ProcessamentoImpl processamento;
		private int seq;
		private String nomeArquivo;
		private String mensagem;
		private int mes,ano,erro;
		private Evento evento;
		private Entidade aseguradora;
		
		public AgendaImpl(ProcessamentoImpl processamento, int seq, String nomeArquivo, String mensagem, int mes, int ano, Evento evento, Entidade aseguradora, int erro)
		{
			this.processamento = processamento;
			this.nomeArquivo = nomeArquivo;
			this.mensagem = mensagem;
			this.mes = mes;
			this.ano = ano;
			this.evento = evento;
			this.aseguradora = aseguradora;
			this.erro = erro;
		}
		
		public int obterSeq()
		{
			return this.seq;
		}

		public String obterNomeArquivo()
		{
			return this.nomeArquivo;
		}

		public String obterMensagem()
		{
			return this.mensagem;
		}

		public int obterMes()
		{
			return this.mes;
		}

		public int obterAno()
		{
			return this.ano;
		}

		public Evento obterEvento()
		{
			return this.evento;
		}
		
		public Entidade obterAseguradora()
		{
			return this.aseguradora;
		}
		
		public boolean agendaComErro()
		{
			return this.erro == 1;
		}
	}
	
	public void atribuirData(Date data)
	{
		this.data = data;
	}
	
	public void incluir() throws Exception
	{
		super.incluir();
		
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into processamento(id,data) values(?,?)");
		insert.addLong(this.obterId());
		insert.addDate(data);
		
		insert.execute();
	}
	
	public Date obterData()
	{
		return this.data;
	}
	
	public void addAgenda(String nomeArquivo, String mensagem, Evento evento, int erro) throws Exception
	{
		int mes = 0;
		int ano = 0;
		long eventoId = 0;
		long origemId = 0;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select max(seq) as mx from processamento_agenda where id = ?");
		query.addLong(this.obterId());
		
		int seq = query.executeAndGetFirstRow().getInt("mx") + 1;
		
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into processamento_agenda(id,seq,nome_arquivo,mensagem,mes,ano,id_agenda, id_aseguradora,erro) values(?,?,?,?,?,?,?,?,?)");
		insert.addLong(this.obterId());
		insert.addInt(seq);
		insert.addString(nomeArquivo);
		insert.addString(mensagem);
		if(evento!=null)
		{
			if(evento instanceof AgendaMovimentacao)
			{
				AgendaMovimentacao agenda = (AgendaMovimentacao) evento;
				
				mes = agenda.obterMesMovimento();
				ano = agenda.obterAnoMovimento();
			}
			else if(evento instanceof Livro)
			{
				Livro livro = (Livro) evento;
				
				mes = livro.obterMes();
				ano = livro.obterAno();
			}
			
			eventoId = evento.obterId();
			origemId = evento.obterOrigem().obterId();
		}
		
		insert.addInt(mes);
		insert.addInt(ano);
		insert.addLong(eventoId);
		insert.addLong(origemId);
		insert.addInt(erro);
		
		insert.execute();
	}
	
	public Collection<Agenda> obterAgendas() throws Exception
	{
		Collection<Agenda> agendas = new ArrayList<Agenda>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select seq,nome_arquivo,mensagem,mes,ano,id_agenda,id_aseguradora,erro from processamento_agenda where id = ? order by seq");
		query.addLong(this.obterId());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			int seq = rows[i].getInt("seq");
			String nomeArquivo = rows[i].getString("nome_arquivo");
			String mensagem = rows[i].getString("mensagem");
			int mes = rows[i].getInt("mes");
			int ano = rows[i].getInt("ano");
			int erro = rows[i].getInt("erro");
			long eventoId = rows[i].getLong("id_agenda");
			long aseguradoraId = rows[i].getLong("id_aseguradora");
			
			Evento evento = null;
			Entidade aseguradora = null;
			
			if(eventoId > 0)
				evento = home.obterEventoPorId(eventoId);
			if(aseguradoraId > 0)
				aseguradora = entidadeHome.obterEntidadePorId(aseguradoraId);
			
			AgendaImpl agenda = new AgendaImpl(this, seq, nomeArquivo, mensagem, mes, ano, evento, aseguradora, erro);
			
			agendas.add(agenda);
		}
		
		return agendas;
	}
	
	public boolean foiProcessadoSemErros(String nomeArquivo) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from processamento_agenda where id = ? nome_arquivo = ? and erro = 0");
		query.addLong(this.obterId());
		query.addString(nomeArquivo);
		
		return query.executeAndGetFirstRow().getInt("qtde") > 0;
	}
	
	public boolean foiProcessado(String nomeArquivo) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from processamento_agenda where id = ? and nome_arquivo = ?");
		query.addLong(this.obterId());
		query.addString(nomeArquivo);
		
		return query.executeAndGetFirstRow().getInt("qtde") > 0;
	}

	public boolean estaNaFila(String nomeArquivo, long dataArquivo) throws Exception
	{
		Date data = new Date(dataArquivo);
		String dataStr = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(data);
		data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataStr);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from rotor_prov where arquivo = ? and data_arquivo = ?");
		query.addString(nomeArquivo);
		query.addDate(data);
		 
		return query.executeAndGetFirstRow().getInt("qtde") > 0;
	}
	
	public void addProcessando(String nomeArquivo, long dataArquivo) throws Exception
	{
		Date data = new Date(dataArquivo);
		String dataStr = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(data);
		data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataStr);
		
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into rotor_prov(arquivo,status,data_arquivo) values(?,?,?)");
		insert.addString(nomeArquivo);
		insert.addString("En la Cola");
		insert.addDate(data);
		
		insert.execute();
	}
	
	public void atualizarProcessando(String nomeArquivo) throws Exception
	{
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","update rotor_prov set status = ? where arquivo = ?");
		insert.addString("En ejecución");
		insert.addString(nomeArquivo);
		
		insert.execute();
	}
	
	public void excluirProcessando(String nomeArquivo) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update rotor_prov set visivel = 1 where arquivo = ?");
		update.addString(nomeArquivo);
		
		update.execute();
	}
}
