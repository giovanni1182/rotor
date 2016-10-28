package com.gio.crm.model;

import java.util.Collection;
import java.util.Date;

public interface Processamento extends Evento
{
	public interface Agenda
	{
		public int obterSeq();
		public String obterNomeArquivo();
		public String obterMensagem();
		public int obterMes();
		public int obterAno();
		Evento obterEvento();
		Entidade obterAseguradora();
		boolean agendaComErro();
	}
	
	void incluir() throws Exception;
	void atribuirData(Date data);
	Date obterData();
	void addAgenda(String nomeArquivo, String mensagem, Evento evento, int erro) throws Exception;
	Collection<Agenda> obterAgendas() throws Exception;
	boolean estaNaFila(String nomeArquivo, long dataArquivo) throws Exception;
	void addProcessando(String nomeArquivo, long dataArquivo) throws Exception;
	void atualizarProcessando(String nomeArquivo) throws Exception;
	void excluirProcessando(String nomeArquivo) throws Exception;
	boolean foiProcessadoSemErros(String nomeArquivo) throws Exception;
	boolean foiProcessado(String nomeArquivo) throws Exception;
}
